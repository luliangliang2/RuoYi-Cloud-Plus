package com.ruoyi.db.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.utils.ServletUtils;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.db.constant.DatabaseConstant;
import com.ruoyi.db.domain.SysDatabaseConfig;
import com.ruoyi.db.mapper.SysDatabaseConfigMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库文档Controller
 *
 * @author lishuyan wrote on 2022/8/28.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/doc")
public class DatabaseDocController {

    private final DataSourceProperties properties;

    private final SysDatabaseConfigMapper configMapper;

    /**
     * 文件输出路径
     */
    private static final String FILE_OUTPUT_DIR = System.getProperty("java.io.tmpdir") + File.separator + "DatabaseDoc";
    /**
     * 文档文件名称
     */
    private static final String DOC_FILE_NAME = "数据库文档";
    /**
     * 版本号
     */
    private static final String DOC_VERSION = "1.2.0";
    /**
     * 文档描述
     */
    private static final String DOC_DESCRIPTION = "文档描述";

    @GetMapping("/exportHtml")
    @Log(title = "数据库文档", businessType = BusinessType.EXPORT)
    @SaCheckPermission("database:doc:export")
    public void exportHtml(@RequestParam(defaultValue = "true") Boolean deleteFile, HttpServletResponse response) {
        doExportFile(EngineFileType.HTML, deleteFile, response);
    }

    @GetMapping("/exportWord")
    @Log(title = "数据库文档", businessType = BusinessType.EXPORT)
    @SaCheckPermission("database:doc:export")
    public void exportWord(@RequestParam(defaultValue = "true") Boolean deleteFile, HttpServletResponse response) {
        doExportFile(EngineFileType.WORD, deleteFile, response);
    }

    @GetMapping("/exportMarkdown")
    @Log(title = "数据库文档", businessType = BusinessType.EXPORT)
    @SaCheckPermission("database:doc:export")
    public void exportMarkdown(@RequestParam(defaultValue = "true") Boolean deleteFile,
                               HttpServletResponse response) {
        doExportFile(EngineFileType.MD, deleteFile, response);
    }

    private void doExportFile(EngineFileType fileOutputType, Boolean deleteFile,
                              HttpServletResponse response) {
        String docFileName = DOC_FILE_NAME + "_" + IdUtil.fastSimpleUUID();
        String filePath = doExportFile(fileOutputType, docFileName);
        //下载后的文件名
        String downloadFileName = DOC_FILE_NAME + fileOutputType.getFileSuffix();
        try {
            // 读取，返回
            ServletUtils.writeAttachment(response, downloadFileName, FileUtil.readBytes(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            handleDeleteFile(deleteFile, filePath);
        }
    }

    /**
     * 输出文件，返回文件路径
     *
     * @param fileOutputType 文件类型
     * @param fileName       文件名, 无需 ".docx" 等文件后缀
     * @return 生成的文件所在路径
     */
    private String doExportFile(EngineFileType fileOutputType, String fileName) {
        try (HikariDataSource dataSource = buildDataSource()) {
            // 创建 screw 的配置
            Configuration config = Configuration.builder()
                // 版本
                .version(DOC_VERSION)
                // 描述
                .description(DOC_DESCRIPTION)
                // 数据源
                .dataSource(dataSource)
                // 引擎配置
                .engineConfig(buildEngineConfig(fileOutputType, fileName))
                // 处理配置
                .produceConfig(buildProcessConfig())
                .build();

            // 执行 screw，生成数据库文档
            new DocumentationExecute(config).execute();

            return FILE_OUTPUT_DIR + File.separator + fileName + fileOutputType.getFileSuffix();
        }
    }

    private void handleDeleteFile(Boolean deleteFile, String filePath) {
        if (!deleteFile) {
            return;
        }
        FileUtil.del(filePath);
    }

    /**
     * 创建数据源
     */
    private HikariDataSource buildDataSource() {
        // 创建 HikariConfig 配置类
        HikariConfig hikariConfig = new HikariConfig();
        // 查询数据库配置
        List<SysDatabaseConfig> configs = configMapper.selectList(new LambdaQueryWrapper<SysDatabaseConfig>()
            .eq(SysDatabaseConfig::getStatus, "0"));
        // 如果没有找到已启用的数据库配置，则使用默认的数据源
        if (configs.isEmpty()) {
            hikariConfig.setJdbcUrl(properties.getUrl());
            hikariConfig.setUsername(properties.getUsername());
            hikariConfig.setPassword(properties.getPassword());
        } else {
            SysDatabaseConfig config = configs.get(0);
            hikariConfig.setJdbcUrl(config.getUrl());
            hikariConfig.setUsername(config.getUsername());
            hikariConfig.setPassword(SecureUtil.aes(DatabaseConstant.KEY).decryptStr(config.getPassword()));
        }
        // 设置可以获取 tables remarks 信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        // 创建数据源
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 创建 screw 的引擎配置
     */
    private static EngineConfig buildEngineConfig(EngineFileType fileOutputType, String docFileName) {
        return EngineConfig.builder()
            // 生成文件路径
            .fileOutputDir(FILE_OUTPUT_DIR)
            // 打开目录
            .openOutputDir(false)
            // 文件类型
            .fileType(fileOutputType)
            // 文件类型
            .produceType(EngineTemplateType.velocity)
            // 自定义文件名称
            .fileName(docFileName)
            .build();
    }

    /**
     * 创建 screw 的处理配置，一般可忽略
     * 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
     */
    private static ProcessConfig buildProcessConfig() {
        return ProcessConfig.builder()
            // 忽略表前缀
            .ignoreTablePrefix(Arrays.asList("QRTZ_", "ACT_"))
            .build();
    }

}
