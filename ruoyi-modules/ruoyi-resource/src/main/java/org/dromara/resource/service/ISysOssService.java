package org.dromara.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.resource.domain.bo.SysOssBo;
import org.dromara.resource.domain.bo.SysOssMultipartBo;
import org.dromara.resource.domain.vo.SysOssMultipartVo;
import org.dromara.resource.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层
 *
 * @author Lion Li
 */
public interface ISysOssService {

    /**
     * 查询OSS对象存储列表
     *
     * @param sysOss    OSS对象存储分页查询对象
     * @param pageQuery 分页查询实体类
     * @return 结果
     */
    TableDataInfo<SysOssVo> queryPageList(SysOssBo sysOss, PageQuery pageQuery);

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    List<SysOssVo> listByIds(Collection<Long> ossIds);

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    String selectUrlByIds(String ossIds);

    /**
     * 查询OSS对象
     */
    SysOssVo getById(Long ossId);

    /**
     * 上传文件
     */
    SysOssVo upload(MultipartFile file);

    /**
     * 上传文件
     */
    SysOssVo upload(File file);

    /**
     * 新增OSS对象
     */
    Boolean insertByBo(SysOssBo bo);

    /**
     * 下载OSS对象存储
     *
     * @param ossId OSS对象ID
     */
    void download(Long ossId, HttpServletResponse response) throws IOException;

    /**
     * 删除OSS对象存储
     *
     * @param ids OSS对象ID串
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 初始化分片上传任务
     *
     * @param originalfileName 文件原名
     * @return 分片上传对象信息
     */
    SysOssMultipartVo initiateMultipart(String originalfileName);

    /**
     * 上传分段
     *
     * @param multipartBo 分片上传对象信息
     * @return 分片上传对象信息
     */
    SysOssMultipartVo uploadPart(MultipartFile file, SysOssMultipartBo multipartBo);

    /**
     * 获取上传分段进度
     *
     * @param multipartBo 分片上传对象信息
     * @return 分片上传对象信息
     */
    SysOssMultipartVo uploadPartList(SysOssMultipartBo multipartBo);

    /**
     * 中止分段上传任务
     *
     * @param multipartBo 分片上传对象信息
     */
    void abortMultipartUpload(SysOssMultipartBo multipartBo);

    /**
     * 合并分段
     *
     * @param multipartBo 分片上传对象信息
     * @return OSS对象存储视图对象
     */
    SysOssVo completeMultipartUpload(SysOssMultipartBo multipartBo);

}
