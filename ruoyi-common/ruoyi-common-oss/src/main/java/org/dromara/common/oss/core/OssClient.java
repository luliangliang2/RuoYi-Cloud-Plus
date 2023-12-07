package org.dromara.common.oss.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.oss.constant.OssConstant;
import org.dromara.common.oss.entity.MultipartUploadResult;
import org.dromara.common.oss.entity.PartUploadResult;
import org.dromara.common.oss.entity.UploadResult;
import org.dromara.common.oss.enumd.AccessPolicyType;
import org.dromara.common.oss.enumd.PolicyType;
import org.dromara.common.oss.exception.OssException;
import org.dromara.common.oss.properties.OssProperties;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * S3 存储协议 所有兼容S3协议的云厂商均支持
 * 阿里云 腾讯云 七牛云 minio
 *
 * @author Lion Li
 */
public class OssClient {

    private final String configKey;

    private final OssProperties properties;

    private final AmazonS3 client;

    /**
     * 构造方法
     *
     * @param configKey     配置键
     * @param ossProperties Oss配置属性
     */
    public OssClient(String configKey, OssProperties ossProperties) {
        this.configKey = configKey;
        this.properties = ossProperties;
        try {
            // 创建 AWS S3 客户端的配置
            AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(properties.getEndpoint(), properties.getRegion());

            // 创建 AWS 认证信息
            AWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey());
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

            // 创建客户端配置
            ClientConfiguration clientConfig = new ClientConfiguration();
            if (OssConstant.IS_HTTPS.equals(properties.getIsHttps())) {
                clientConfig.setProtocol(Protocol.HTTPS);
            } else {
                clientConfig.setProtocol(Protocol.HTTP);
            }

            // 使用 AWS S3 客户端构建器创建客户端
            AmazonS3ClientBuilder build = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(clientConfig)
                .withCredentials(credentialsProvider)
                .disableChunkedEncoding();

            // 针对不同的云服务或存储服务进行特殊配置
            if (!StringUtils.containsAny(properties.getEndpoint(), OssConstant.CLOUD_SERVICE)) {
                // minio 使用https限制使用域名访问 需要此配置 站点填域名
                build.enablePathStyleAccess();
            }

            // 使用构建器创建 AWS S3 客户端
            this.client = build.build();

            // 创建存储桶
            createBucket();
        } catch (Exception e) {
            if (e instanceof OssException) {
                throw e;
            }
            throw new OssException("配置错误! 请检查系统配置:[" + e.getMessage() + "]");
        }
    }

    /**
     * 生成 AWS S3 存储桶访问策略
     *
     * @param bucketName 存储桶
     * @param policyType 桶策略类型
     * @return 符合 AWS S3 存储桶访问策略格式的字符串
     */
    private static String getPolicy(String bucketName, PolicyType policyType) {
        StringBuilder builder = new StringBuilder();
        //开始构建 JSON 格式的访问策略
        builder.append("{\n\"Statement\": [\n{\n\"Action\": [\n");

        //根据传入的策略类型生成不同的 Action 部分，包括对于读、写、读写等不同权限的配置。
        builder.append(switch (policyType) {
            case WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucketMultipartUploads\"\n";
            case READ_WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucket\",\n\"s3:ListBucketMultipartUploads\"\n";
            default -> "\"s3:GetBucketLocation\"\n";
        });
        builder.append("],\n\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n},\n");

        // 如果策略类型是 READ，则添加一个 Deny 的 Statement，限制 ListBucket 的权限
        if (policyType == PolicyType.READ) {
            builder.append("{\n\"Action\": [\n\"s3:ListBucket\"\n],\n\"Effect\": \"Deny\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n},\n");
        }

        // 添加允许访问对象的 Statement
        builder.append("{\n\"Action\": ");
        //始构建允许访问对象的 Statement
        builder.append(switch (policyType) {
            case WRITE ->
                "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n";
            case READ_WRITE ->
                "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:GetObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n";
            default -> "\"s3:GetObject\",\n";
        });
        //设置 Statement 的 Effect、Principal 和 Resource
        builder.append("\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        //将存储桶名称添加到 Resource 的末尾
        builder.append(bucketName);
        //完成 JSON 结构
        builder.append("/*\"\n}\n],\n\"Version\": \"2012-10-17\"\n}\n");
        //符合 AWS S3 存储桶访问策略格式的字符串
        return builder.toString();
    }

    /**
     * 创建存储桶
     */
    public void createBucket() {
        try {
            // 获取存储桶名称
            String bucketName = properties.getBucketName();
            // 如果存储桶已存在，则直接返回，不再创建
            if (client.doesBucketExistV2(bucketName)) {
                return;
            }
            // 创建存储桶请求
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            // 获取存储桶的访问策略（ACL）
            AccessPolicyType accessPolicy = getAccessPolicy();
            createBucketRequest.setCannedAcl(accessPolicy.getAcl());
            // 创建存储桶
            client.createBucket(createBucketRequest);
            // 设置存储桶的访问策略（Bucket Policy）
            client.setBucketPolicy(bucketName, getPolicy(bucketName, accessPolicy.getPolicyType()));
        } catch (Exception e) {
            throw new OssException("创建Bucket失败, 请核对配置信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 使用字节数组上传文件
     *
     * @param data        字节数组
     * @param path        用于存储新对象的键
     * @param contentType 上传文件内容类型
     * @return 上传返回体
     */
    public UploadResult upload(byte[] data, String path, String contentType) {
        return upload(new ByteArrayInputStream(data), path, contentType);
    }

    /**
     * 使用输入流上传文件
     *
     * @param inputStream 输入流
     * @param path        用于存储新对象的键
     * @param contentType 上传文件内容类型
     * @return 上传返回体
     */
    public UploadResult upload(InputStream inputStream, String path, String contentType) {
        // 如果输入流不是 ByteArrayInputStream，将其读取为字节数组，并重新包装成 ByteArrayInputStream
        if (!(inputStream instanceof ByteArrayInputStream)) {
            inputStream = new ByteArrayInputStream(IoUtil.readBytes(inputStream));
        }
        try {
            // 创建上传对象的元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(inputStream.available());

            // 创建上传对象的请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), path, inputStream, metadata);
            // 设置上传对象的 Acl 为公共读
            putObjectRequest.setCannedAcl(getAccessPolicy().getAcl());
            // 执行上传操作
            client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
        // 返回上传结果对象
        return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build();
    }

    /**
     * 删除云存储服务中指定路径下文件
     *
     * @param path 指定路径
     */
    public void delete(String path) {
        // 将路径中的基础URL部分去除，得到相对路径
        path = path.replace(getUrl() + "/", "");
        try {
            // 调用云存储服务的删除对象方法
            client.deleteObject(properties.getBucketName(), path);
        } catch (Exception e) {
            throw new OssException("删除文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 使用字节数组上传文件
     *
     * @param data        文件数据
     * @param suffix      后缀
     * @param contentType 类型
     * @return 上传返回体
     */
    public UploadResult uploadSuffix(byte[] data, String suffix, String contentType) {
        return upload(data, getPath(properties.getPrefix(), suffix), contentType);
    }

    /**
     * 使用输入流上传文件
     *
     * @param inputStream 输入流
     * @param suffix      后缀
     * @param contentType 类型
     * @return 上传返回体
     */
    public UploadResult uploadSuffix(InputStream inputStream, String suffix, String contentType) {
        return upload(inputStream, getPath(properties.getPrefix(), suffix), contentType);
    }

    /**
     * 使用文件对象上传文件
     *
     * @param file   文件
     * @param suffix 后缀
     * @return 上传返回体
     */
    public UploadResult uploadSuffix(File file, String suffix) {
        return upload(file, getPath(properties.getPrefix(), suffix));
    }

    /**
     * 获取文件元数据
     *
     * @param path 完整文件路径
     */
    public ObjectMetadata getObjectMetadata(String path) {
        path = path.replace(getUrl() + "/", "");
        S3Object object = client.getObject(properties.getBucketName(), path);
        return object.getObjectMetadata();
    }

    /**
     * 获取文件输入流
     *
     * @param path 完整文件路径
     * @return 输入流
     */
    public InputStream getObjectContent(String path) {
        // 将路径中的基础URL部分去除，得到相对路径
        path = path.replace(getUrl() + "/", "");
        // 使用 Amazon S3 客户端从指定的存储桶（Bucket）和相对路径（path）获取对象
        S3Object object = client.getObject(properties.getBucketName(), path);
        // 返回对象的内容作为输入流
        return object.getObjectContent();
    }

    /**
     * 获取云存储服务的URL
     *
     * @return 文件路径
     */
    public String getUrl() {
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        String header = OssConstant.IS_HTTPS.equals(properties.getIsHttps()) ? "https://" : "http://";
        // 云服务商直接返回
        if (StringUtils.containsAny(endpoint, OssConstant.CLOUD_SERVICE)) {
            if (StringUtils.isNotBlank(domain)) {
                return header + domain;
            }
            return header + properties.getBucketName() + "." + endpoint;
        }
        // minio 单独处理
        if (StringUtils.isNotBlank(domain)) {
            return header + domain + "/" + properties.getBucketName();
        }
        // 对于其他情况，拼接 endpoint 和 bucketName 构成 URL
        return header + endpoint + "/" + properties.getBucketName();
    }

    /**
     * 生成一个符合特定规则的、唯一的文件路径。通过使用日期、UUID、前缀和后缀等元素的组合，确保了文件路径的独一无二性
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 文件路径
     */
    public String getPath(String prefix, String suffix) {
        // 生成uuid
        String uuid = IdUtil.fastSimpleUUID();
        // 文件路径
        String path = DateUtils.datePath() + "/" + uuid;

        // 如果有前缀，则加上前缀
        if (StringUtils.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }
        // 加上后缀
        return path + suffix;
    }


    public String getConfigKey() {
        return configKey;
    }

    /**
     * 获取私有URL链接
     *
     * @param objectKey 对象KEY
     * @param second    授权时间
     */
    public String getPrivateUrl(String objectKey, Integer second) {
        // 创建生成预签名URL的请求对象
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(properties.getBucketName(), objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(System.currentTimeMillis() + 1000L * second));
        // 生成预签名URL
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        // 将URL转换为字符串并返回
        return url.toString();
    }

    /**
     * 使用文件对象上传文件（适用于本地文件系统）
     *
     * @param file 要上传到 Amazon S3 的文件的路径
     * @param path 用于存储新对象的键
     * @return 上传返回体
     */
    public UploadResult upload(File file, String path) {
        try {
            // 创建上传对象的请求，使用文件作为上传的内容
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), path, file);
            // 设置上传对象的 Acl 为公共读
            putObjectRequest.setCannedAcl(getAccessPolicy().getAcl());
            // 执行上传操作
            client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
        return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build();
    }

    /**
     * 获取当前桶权限类型
     *
     * @return 当前桶权限类型code
     */
    public AccessPolicyType getAccessPolicy() {
        return AccessPolicyType.getByType(properties.getAccessPolicy());
    }

    /**
     * 检查配置是否相同
     *
     * @param properties 配置属性
     * @return 是否相同
     */
    public boolean checkPropertiesSame(OssProperties properties) {
        return this.properties.equals(properties);
    }

    /**
     * 初始化分片上传任务
     *
     * @param suffix 文件后缀
     * @return 包含上传任务信息的对象
     */
    public MultipartUploadResult initiateMultipart(String suffix) {
        // 生成唯一的键，使用文件前缀和指定的后缀
        String path = getPath(properties.getPrefix(), suffix);
        // 创建分片上传请求对象
        InitiateMultipartUploadRequest multipartUploadRequest = new InitiateMultipartUploadRequest(properties.getBucketName(), path);
        // 执行分片上传初始化，并获取初始化响应
        InitiateMultipartUploadResult initResponse = client.initiateMultipartUpload(multipartUploadRequest);
        // 构建并返回包含上传任务信息的对象
        return MultipartUploadResult.builder().uploadId(initResponse.getUploadId()).filename(path).build();
    }

    /**
     * 获取上传分段列表
     *
     * @param uploadId 新分段上传的唯一 ID
     * @param path     文件路径
     * @return 上传分段结果列表，如果没有上传分段则返回空列表
     */
    public List<PartUploadResult> uploadPartList(String uploadId, String path) {
        // 获取上传分段的摘要信息列表
        List<PartSummary> list = listParts(uploadId, path);
        // 判断是否有上传分段
        if (CollUtil.isNotEmpty(list)) {
            // 将分段摘要信息转换为上传分段结果对象列表
            return list.stream()
                .map(x -> PartUploadResult.builder()
                    .partNumber(x.getPartNumber())
                    .eTag(x.getETag()).build())
                .collect(Collectors.toList());
        }
        // 没有上传分段，则返回空列表
        return Collections.emptyList();
    }

    /**
     * 上传分段
     *
     * @param inputStream 输入流
     * @param uploadId    新分段上传的唯一 ID
     * @param path        文件路径
     * @param partNumber  部分号，从1开始递增
     * @param partSize    此部分的大小
     * @param md5Digest   MD5 哈希值
     * @return 上传部件的结果对象
     * @throws OssException 如果上传分段失败，抛出自定义异常
     */
    public PartUploadResult uploadPart(InputStream inputStream, String uploadId, String path, Integer partNumber, Long partSize, String md5Digest) {
        UploadPartResult uploadPartResult;
        // 如果输入流不是 ByteArrayInputStream，将其读取为字节数组，并重新包装成 ByteArrayInputStream
        if (!(inputStream instanceof ByteArrayInputStream)) {
            inputStream = new ByteArrayInputStream(IoUtil.readBytes(inputStream));
        }
        try {
            // 创建上传分段请求对象
            UploadPartRequest uploadPart = new UploadPartRequest();
            // 设置分段上传的唯一 ID
            uploadPart.setUploadId(uploadId);
            // 设置文件路径
            uploadPart.setKey(path);
            // 设置存储桶名称
            uploadPart.setBucketName(properties.getBucketName());
            // 设置部分号，从1开始递增
            uploadPart.setPartNumber(partNumber);
            // 设置此部分的大小
            uploadPart.setPartSize(partSize);
            // 设置MD5哈希值
            uploadPart.setMd5Digest(md5Digest);
            // 设置待上传的文件
            uploadPart.setInputStream(inputStream);
            // 执行上传分段操作，并获取上传分段的响应
            uploadPartResult = client.uploadPart(uploadPart);
        } catch (Exception e) {
            // 捕获并处理异常，抛出自定义异常
            throw new OssException("上传分段失败，请检查配置信息:[" + e.getMessage() + "]");
        }
        // 构建并返回上传部件的结果对象
        return PartUploadResult.builder().partNumber(uploadPartResult.getPartNumber()).eTag(uploadPartResult.getETag()).build();
    }

    /**
     * 中止 Amazon S3 中的分段上传任务。
     *
     * @param uploadId 分段上传的唯一 ID
     * @param path     文件路径
     * @throws OssException 如果中止分段上传失败，抛出自定义异常，包含错误信息
     */
    public void abortMultipartUpload(String uploadId, String path) {
        // 创建中止分段上传请求对象
        AbortMultipartUploadRequest abortMultipart = new AbortMultipartUploadRequest(properties.getBucketName(), uploadId, path);
        try {
            // 执行中止分段上传操作
            client.abortMultipartUpload(abortMultipart);
        } catch (Exception e) {
            // 捕获并处理异常，抛出自定义异常
            throw new OssException("中止分段上传失败，请检查配置信息:[" + e.getMessage() + "]");
        }
    }


    /**
     * 完成分段上传，合并分段文件
     *
     * @param uploadId 新分段上传的唯一 ID
     * @param path     文件路径
     * @return 完成上传的结果对象
     * @throws OssException 如果合并文件失败，抛出自定义异常
     */
    public UploadResult completeMultipartUpload(String uploadId, String path) {
        try {
            // 获取上传分段的摘要信息列表
            List<PartSummary> partListing = listParts(uploadId, path);
            // 初始化上传部件的 ETag 列表
            List<PartETag> collect;
            // 判断是否有上传分段
            if (CollUtil.isNotEmpty(partListing)) {
                // 将分段摘要信息转换为上传部件的 ETag 列表
                collect = partListing
                    .stream()
                    .map(x -> new PartETag(x.getPartNumber(), x.getETag()))
                    .collect(Collectors.toList());
            } else {
                // 没有上传分段，抛出自定义异常
                throw new OssException("合并文件失败，分片数据异常");
            }
            // 创建完成分段上传请求对象
            CompleteMultipartUploadRequest completeMultipart = new CompleteMultipartUploadRequest();
            completeMultipart.setBucketName(properties.getBucketName());
            completeMultipart.setUploadId(uploadId);
            completeMultipart.setKey(path);
            completeMultipart.setPartETags(collect);
            // 执行完成分段上传操作
            client.completeMultipartUpload(completeMultipart);
        } catch (Exception e) {
            // 捕获并处理异常，抛出自定义异常
            throw new OssException("合并文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
        // 构建并返回上传结果对象，包含文件的 URL 和文件名
        return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build();
    }


    /**
     * 列出已为特定分段上传上传的分段
     *
     * @param uploadId 新分段上传的唯一 ID
     * @param key      文件名
     * @return 已上传的分段的摘要信息列表
     * @throws OssException 如果查询分片失败，抛出自定义异常
     */
    private List<PartSummary> listParts(String uploadId, String key) {
        try {
            ListPartsRequest listParts;
            List<PartSummary> parts = new ArrayList<>();
            // 使用 do-while 循环迭代列出分段的请求
            do {
                // 创建列出分段的请求对象
                listParts = new ListPartsRequest(properties.getBucketName(), key, uploadId);
                // 执行列出分段的操作，并获取列出的分段信息（最多返回 1000 个）
                PartListing partListing = client.listParts(listParts);
                // 判断是否有分段信息
                if (ObjectUtil.isNotNull(partListing) && CollUtil.isNotEmpty(partListing.getParts())) {
                    // 将分段信息添加到列表中
                    parts.addAll(partListing.getParts());
                    // 设置下一个请求的起始部分号
                    listParts.setPartNumberMarker(parts.get(parts.size() - 1).getPartNumber());
                } else {
                    // 没有更多分段需要检索，跳出循环
                    break;
                }
            } while (true);
            // 返回已上传的分段的摘要信息列表
            return parts;
        } catch (Exception e) {
        }
        // 没有上传分段，则返回空列表
        return Collections.emptyList();
    }

}
