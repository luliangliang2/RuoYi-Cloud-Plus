package org.dromara.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.file.FileUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.oss.core.OssClient;
import org.dromara.common.oss.entity.MultipartUploadResult;
import org.dromara.common.oss.entity.PartUploadResult;
import org.dromara.common.oss.entity.UploadResult;
import org.dromara.common.oss.enumd.AccessPolicyType;
import org.dromara.common.oss.factory.OssFactory;
import org.dromara.resource.domain.SysOss;
import org.dromara.resource.domain.bo.SysOssBo;
import org.dromara.resource.domain.bo.SysOssMultipartBo;
import org.dromara.resource.domain.vo.SysOssMultipartVo;
import org.dromara.resource.domain.vo.SysOssVo;
import org.dromara.resource.mapper.SysOssMapper;
import org.dromara.resource.service.ISysOssService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysOssServiceImpl implements ISysOssService {

    private final SysOssMapper baseMapper;

    /**
     * 查询OSS对象存储列表
     *
     * @param bo        OSS对象存储分页查询对象
     * @param pageQuery 分页查询实体类
     * @return 结果
     */
    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysOssVo> filterResult = result.getRecords().stream().map(this::matchingUrl).collect(Collectors.toList());
        result.setRecords(filterResult);
        return TableDataInfo.build(result);
    }

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    @Override
    public List<SysOssVo> listByIds(Collection<Long> ossIds) {
        List<SysOssVo> list = new ArrayList<>();
        for (Long id : ossIds) {
            SysOssVo vo = getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    list.add(this.matchingUrl(vo));
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(vo);
                }
            }
        }
        return list;
    }

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    @Override
    public String selectUrlByIds(String ossIds) {
        List<String> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(ossIds, Convert::toLong)) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    list.add(this.matchingUrl(vo).getUrl());
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(vo.getUrl());
                }
            }
        }
        return String.join(StringUtils.SEPARATOR, list);
    }

    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.like(StringUtils.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(ObjectUtil.isNotNull(bo.getCreateBy()), SysOss::getCreateBy, bo.getCreateBy());
        lqw.eq(StringUtils.isNotBlank(bo.getService()), SysOss::getService, bo.getService());
        lqw.orderByAsc(SysOss::getOssId);
        return lqw;
    }

    /**
     * 查询单个
     */
    @Cacheable(cacheNames = CacheNames.SYS_OSS, key = "#ossId")
    @Override
    public SysOssVo getById(Long ossId) {
        return baseMapper.selectVoById(ossId);
    }

    /**
     * 下载OSS对象存储
     *
     * @param ossId OSS对象ID
     */
    @Override
    public void download(Long ossId, HttpServletResponse response) throws IOException {
        SysOssVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        //获取实例
        OssClient storage = OssFactory.instance(sysOss.getService());
        try(InputStream inputStream = storage.getObjectContent(sysOss.getUrl())) {
            // 获取输入流的可用字节数
            int available = inputStream.available();
            // 将输入流内容复制到 HTTP 响应的输出流中
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            // 设置响应的内容长度
            response.setContentLength(available);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 上传文件
     */
    @Override
    public SysOssVo upload(MultipartFile file) {
        //获取文件原名
        String originalfileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        //获取默认实例
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult);
    }

    /**
     * 上传文件
     */
    @Override
    public SysOssVo upload(File file) {
        //获取文件原名
        String originalfileName = file.getName();
        //获取文件后缀名
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        //获取默认实例
        OssClient storage = OssFactory.instance();
        //使用文件对象上传文件
        UploadResult uploadResult = storage.uploadSuffix(file, suffix);
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult);
    }

    /**
     * 新增并返回OSS对象
     *
     * @param originalfileName 原名
     * @param suffix           文件后缀名
     * @param configKey        服务商
     * @param uploadResult     上传返回体
     * @return OSS对象
     */
    private SysOssVo buildResultEntity(String originalfileName, String suffix, String configKey, UploadResult uploadResult) {
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalfileName);
        oss.setService(configKey);
        baseMapper.insert(oss);
        SysOssVo sysOssVo = MapstructUtils.convert(oss, SysOssVo.class);
        return this.matchingUrl(sysOssVo);
    }

    /**
     * 新增OSS对象
     */
    @Override
    public Boolean insertByBo(SysOssBo bo) {
        SysOss oss = BeanUtil.toBean(bo, SysOss.class);
        boolean flag = baseMapper.insert(oss) > 0;
        if (flag) {
            bo.setOssId(oss.getOssId());
        }
        return flag;
    }

    /**
     * 删除OSS对象存储
     *
     * @param ids OSS对象ID串
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysOss> list = baseMapper.selectBatchIds(ids);
        for (SysOss sysOss : list) {
            OssClient storage = OssFactory.instance(sysOss.getService());
            storage.delete(sysOss.getUrl());
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 初始化分片上传任务
     *
     * @param originalfileName 文件原名
     * @return 分片上传对象信息
     */
    @Override
    public SysOssMultipartVo initiateMultipart(String originalfileName) {
        //获取文件后缀名
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        //获取默认实例
        OssClient storage = OssFactory.instance();
        //创建上传任务
        MultipartUploadResult multipart = storage.initiateMultipart(suffix);
        SysOssMultipartVo multipartVo = new SysOssMultipartVo();
        multipartVo.setUploadId(multipart.getUploadId());
        multipartVo.setFilename(multipart.getFilename());
        return multipartVo;
    }

    /**
     * 上传分段
     */
    @Override
    public SysOssMultipartVo uploadPart(MultipartFile file, SysOssMultipartBo multipartBo) {
        //获取文件原名
        String uploadId = multipartBo.getUploadId();
        String filename = multipartBo.getFilename();
        OssClient storage = OssFactory.instance();
        PartUploadResult uploadPart;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
            uploadPart = storage.uploadPart(byteArrayInputStream, uploadId, filename, multipartBo.getPartNumber(), multipartBo.getPartSize(), multipartBo.getMd5Digest());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        SysOssMultipartVo multipartVo = new SysOssMultipartVo();
        multipartVo.setUploadId(uploadId);
        multipartVo.setFilename(filename);
        multipartVo.setPartNumber(uploadPart.getPartNumber());
        multipartVo.setETag(uploadPart.getETag());
        return multipartVo;
    }

    /**
     * 获取上传分段进度
     *
     * @param multipartBo 分片上传对象信息
     * @return 分片上传对象信息
     */
    @Override
    public SysOssMultipartVo uploadPartList(SysOssMultipartBo multipartBo) {
        OssClient storage = OssFactory.instance();
        List<PartUploadResult> partList = storage.uploadPartList(multipartBo.getUploadId(), multipartBo.getFilename());
        SysOssMultipartVo multipartVo = new SysOssMultipartVo();
        if (CollUtil.isNotEmpty(partList)) {
            multipartVo.setPartUploadList(
                partList.stream().map(
                        x -> new SysOssMultipartVo.PartUploadResult(x.getPartNumber(), x.getETag()))
                    .collect(Collectors.toList()));
        }
        return multipartVo;
    }

    /**
     * 中止分段上传任务
     *
     * @param multipartBo 分片上传对象信息
     */
    @Override
    public void abortMultipartUpload(SysOssMultipartBo multipartBo) {
        OssClient storage = OssFactory.instance();
        storage.abortMultipartUpload(multipartBo.getUploadId(), multipartBo.getFilename());
    }

    /**
     * 合并分段
     *
     * @param multipartBo 分片上传对象信息
     * @return OSS对象存储视图对象
     */
    @Override
    public SysOssVo completeMultipartUpload(SysOssMultipartBo multipartBo) {
        //获取文件原名
        String originalfileName = multipartBo.getOriginalfileName();
        //获取文件后缀名
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        //获取默认实例
        OssClient storage = OssFactory.instance();
        //合并分段
        UploadResult uploadResult = storage.completeMultipartUpload(multipartBo.getUploadId(), multipartBo.getFilename());
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult);
    }

    /**
     * 桶类型为 private 的URL 修改为临时URL时长为120s
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysOssVo matchingUrl(SysOssVo oss) {
        //根据类型获取实例
        OssClient storage = OssFactory.instance(oss.getService());
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
            oss.setUrl(storage.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }

}
