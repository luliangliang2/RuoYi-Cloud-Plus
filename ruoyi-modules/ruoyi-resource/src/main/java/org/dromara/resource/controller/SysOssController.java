package org.dromara.resource.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.core.validate.QueryGroup;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.resource.domain.bo.SysOssBo;
import org.dromara.resource.domain.bo.SysOssMultipartBo;
import org.dromara.resource.domain.vo.SysOssMultipartVo;
import org.dromara.resource.domain.vo.SysOssUploadVo;
import org.dromara.resource.domain.vo.SysOssVo;
import org.dromara.resource.service.ISysOssService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传 控制层
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oss")
public class SysOssController extends BaseController {

    private final ISysOssService iSysOssService;

    /**
     * 查询OSS对象存储列表
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public TableDataInfo<SysOssVo> list(@Validated(QueryGroup.class) SysOssBo bo, PageQuery pageQuery) {
        return iSysOssService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/listByIds/{ossIds}")
    public R<List<SysOssVo>> listByIds(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ossIds) {
        List<SysOssVo> list = iSysOssService.listByIds(Arrays.asList(ossIds));
        return R.ok(list);
    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     */
    @SaCheckPermission("system:oss:upload")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<SysOssUploadVo> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            return R.fail("上传文件不能为空");
        }
        SysOssVo oss = iSysOssService.upload(file);
        SysOssUploadVo uploadVo = new SysOssUploadVo();
        uploadVo.setUrl(oss.getUrl());
        uploadVo.setFileName(oss.getOriginalName());
        uploadVo.setOssId(oss.getOssId().toString());
        return R.ok(uploadVo);
    }

    /**
     * 下载OSS对象存储
     *
     * @param ossId OSS对象ID
     */
    @SaCheckPermission("system:oss:download")
    @GetMapping("/download/{ossId}")
    public void download(@PathVariable Long ossId, HttpServletResponse response) throws IOException {
        iSysOssService.download(ossId, response);
    }

    /**
     * 删除OSS对象存储
     *
     * @param ossIds OSS对象ID串
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ossIds) {
        return toAjax(iSysOssService.deleteWithValidByIds(Arrays.asList(ossIds), true));
    }

    /**
     * 初始化分片上传任务
     *
     * @param originalfileName 文件原名
     */
    @SaCheckPermission("system:oss:multipart")
    @PostMapping(value = "/multipart/initiate")
    public R<SysOssMultipartVo> initiateMultipart(@NotBlank(message = "文件原名不能为空")
                                                  @Size(min = 1, max = 255, message = "文件原名长度必须在1到255之间") String originalfileName) {
        return R.ok(iSysOssService.initiateMultipart(originalfileName));
    }

    /**
     * 上传分段
     */
    @SaCheckPermission("system:oss:multipart")
    @PostMapping(value = "/multipart/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<SysOssMultipartVo> uploadPart(@RequestPart("file") MultipartFile file, @Validated(AddGroup.class) @RequestBody SysOssMultipartBo multipartBo) {
        return R.ok(iSysOssService.uploadPart(file, multipartBo));
    }

    /**
     * 查询上传分段进度
     */
    @SaCheckPermission("system:oss:multipart")
    @PostMapping(value = "/multipart/list")
    public R<SysOssMultipartVo> uploadPartList(@Validated(QueryGroup.class) @RequestBody SysOssMultipartBo multipartBo) {
        return R.ok(iSysOssService.uploadPartList(multipartBo));
    }

    /**
     * 中止分段上传任务（需要多次执行）
     */
    @SaCheckPermission("system:oss:multipart")
    @PutMapping(value = "/multipart/abort")
    public R<Void> abortMultipartUpload(@Validated(QueryGroup.class) @RequestBody SysOssMultipartBo multipartBo) {
        iSysOssService.abortMultipartUpload(multipartBo);
        return R.ok();
    }

    /**
     * 合并分段文件
     */
    @SaCheckPermission("system:oss:multipart")
    @Log(title = "OSS对象存储-分片", businessType = BusinessType.INSERT)
    @PostMapping(value = "/multipart/complete")
    public R<SysOssVo> completeMultipartUpload(@Validated(EditGroup.class) @RequestBody SysOssMultipartBo multipartBo) {
        return R.ok(iSysOssService.completeMultipartUpload(multipartBo));
    }

}
