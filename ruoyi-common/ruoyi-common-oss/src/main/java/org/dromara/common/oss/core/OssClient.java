package org.dromara.common.oss.core;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.file.FileUtils;
import org.dromara.common.oss.constant.OssConstant;
import org.dromara.common.oss.entity.UploadResult;
import org.dromara.common.oss.enumd.AccessPolicyType;
import org.dromara.common.oss.enumd.PolicyType;
import org.dromara.common.oss.exception.OssException;
import org.dromara.common.oss.properties.OssProperties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.BlockingInputStreamAsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.*;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * S3 存储协议 所有兼容S3协议的云厂商均支持
 * 阿里云 腾讯云 七牛云 minio
 *
 * @author Lion Li
 */
public class OssClient {

    /**
     * 服务商
     */
    private final String configKey;

    /**
     * 配置属性
     */
    private final OssProperties properties;

    /**
     * Amazon S3 异步客户端
     */
    private final S3AsyncClient client;

    /**
     * 用于管理 S3 数据传输的高级工具
     */
    private final S3TransferManager transferManager;

    /**
     * AWS S3 预签名 URL 的生成器
     */
    private final S3Presigner presigner;

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
            // 创建 AWS 认证信息
            AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey());
            StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);

            //创建AWS基于 CRT 的 S3 客户端
            S3AsyncClient client = S3AsyncClient.crtBuilder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(getEndpoint()))
                .region(of())
                .targetThroughputInGbps(20.0)
                .minimumPartSizeInBytes(10 * 1025 * 1024L)
                .checksumValidationEnabled(false)
                .build();

            // 创建 S3Configuration.Builder 对象用于配置 S3Presigner 的行为
            S3Configuration.Builder config = S3Configuration.builder().chunkedEncodingEnabled(false);
            // 检查是否连接到 MinIO，MinIO 使用 HTTPS 限制使用域名访问，需要启用路径样式访问
            if (!StringUtils.containsAny(properties.getEndpoint(), OssConstant.CLOUD_SERVICE)) {
                // minio 使用https限制使用域名访问 需要此配置 站点填域名
                config.pathStyleAccessEnabled(true);
            }

            // 创建 S3Presigner 实例，用于生成 S3 预签名 URL
            S3Presigner presigner = S3Presigner.builder()
                .region(of())
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(getDomain()))
                .serviceConfiguration(config.build())
                .build();

            //AWS基于 CRT 的 S3 AsyncClient 实例用作 S3 传输管理器的底层客户端
            this.client = client;
            this.transferManager = S3TransferManager.builder().s3Client(client).build();
            this.presigner = presigner;
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
     * 同步创建存储桶
     * 如果存储桶不存在，会进行创建；如果存储桶存在，不执行任何操作
     *
     * @throws OssException 当创建存储桶时发生异常时抛出
     */
    public void createBucket() {
        String bucketName = properties.getBucketName();
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
        try {
            client.headBucket(headBucketRequest).join();
            // 存储桶存在
        } catch (Exception ex) {
            if (ex.getCause() instanceof NoSuchBucketException) {
                // 存储桶不存在，进行创建
                CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket(bucketName).build();
                // 设置存储桶的访问策略（Bucket Policy）
                PutBucketPolicyRequest putBucketPolicyRequest = PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(getPolicy(bucketName, getAccessPolicy().getPolicyType()))
                    .build();
                try {
                    // 创建存储桶
                    client.createBucket(createBucketRequest).join();
                    // 设置存储桶的访问策略（Bucket Policy）
                    client.putBucketPolicy(putBucketPolicyRequest).join();
                } catch (S3Exception e) {
                    throw new OssException("创建Bucket失败, 请核对配置信息:[" + e.getMessage() + "]");
                }
            } else if (ex.getCause() instanceof S3Exception) {
                throw new OssException("判断Bucket是否存在失败，请核对配置信息::[" + ex.getMessage() + "]");
            } else {
                // 其他异常
                throw new OssException("判断存储桶是否存在时发生异常:[" + ex.getMessage() + "]");
            }
        }
    }

    /**
     * 上传文件到 Amazon S3，并返回上传结果
     *
     * @param filePath  本地文件路径
     * @param key       在 Amazon S3 中的对象键
     * @param md5Digest 本地文件的 MD5 哈希值（可选）
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult upload(Path filePath, String key, String md5Digest) {
        // 创建上传请求
        PutObjectRequest.Builder putObjectRequest = PutObjectRequest.builder().bucket(properties.getBucketName()).key(key);

        // 如果提供了 md5Digest，设置 contentMD5
        if (StringUtils.isNotEmpty(md5Digest)) {
            putObjectRequest.contentMD5(md5Digest);
        }

        // 构建上传文件请求
        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
            .putObjectRequest(putObjectRequest.build())
            .addTransferListener(LoggingTransferListener.create())
            .source(filePath)
            .build();

        // 构建 UploadResult 对象
        UploadResult.UploadResultBuilder upload = UploadResult.builder().url(getUrl() + "/" + key).filename(key);
        try {
            // 执行文件上传
            FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);
            CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
            // 将上传结果中的 ETag 添加到 UploadResult
            upload.eTag(uploadResult.response().eTag());
        } catch (Exception e) {
            // 捕获异常并抛出自定义异常
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        } finally {
            // 删除临时文件
            FileUtils.del(filePath);
        }
        // 返回最终的 UploadResult
        return upload.build();
    }

    /**
     * 上传 InputStream 到 Amazon S3
     *
     * @param inputStream 要上传的输入流
     * @param path        在 Amazon S3 中的对象键
     * @param length      输入流的长度
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult upload(InputStream inputStream, String path, Long length) {
        // 如果输入流不是 ByteArrayInputStream，则将其读取为字节数组再创建 ByteArrayInputStream
        if (!(inputStream instanceof ByteArrayInputStream)) {
            inputStream = new ByteArrayInputStream(IoUtil.readBytes(inputStream));
        }
        try {
            BlockingInputStreamAsyncRequestBody body = AsyncRequestBody.forBlockingInputStream(length);

            // 构造上传对象的请求
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(properties.getBucketName())
                .key(path)
                .build();

            // 使用 transferManager 进行上传
            Upload upload = transferManager.upload(builder -> builder
                .requestBody(body)
                .putObjectRequest(putObjectRequest)
                .build());
            // 该对象将输入流包装为适用于 S3 上传的请求体
            body.writeInputStream(inputStream);

            // 等待文件上传操作完成
            upload.completionFuture().join();
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
        return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build();
    }

    /**
     * 下载文件从 Amazon S3 到临时目录
     *
     * @param path 文件在 Amazon S3 中的对象键
     * @return 下载后的文件在本地的临时路径
     * @throws OssException 如果下载失败，抛出自定义异常
     */
    public Path fileDownload(String path) {
        // 从路径中移除 URL 前缀
        path = removeBaseUrl(path);

        // 构建获取对象的请求
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(properties.getBucketName())
            .key(path)
            .build();

        // 构建临时文件路径
        Path tempFilePath = Paths.get(FileUtils.getTmpDirPath() + extractFileName(path));
        // 构建文件下载请求
        DownloadFileRequest downloadFileRequest =
            DownloadFileRequest.builder()
                .getObjectRequest(getObjectRequest)
                .addTransferListener(LoggingTransferListener.create())
                .destination(tempFilePath)
                .build();

        // 使用 S3TransferManager 下载文件
        FileDownload downloadFile = transferManager.downloadFile(downloadFileRequest);
        // 等待文件下载操作完成
        downloadFile.completionFuture().join();
        return tempFilePath;
    }

    /**
     * 删除云存储服务中指定路径下文件
     *
     * @param path 指定路径
     */
    public void delete(String path) {
        path = removeBaseUrl(path);
        try {
            // 构建 DeleteObjectRequest 对象
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(properties.getBucketName())
                .key(path)
                .build();
            client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new OssException("删除文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 获取私有URL链接
     *
     * @param objectKey 对象KEY
     * @param second    授权时间
     */
    public String getPrivateUrl(String objectKey, Integer second) {
        // 构造获取对象的请求
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(properties.getBucketName())
            .key(objectKey)
            .build();

        // 构造获取对象预签名请求
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(second))
            .getObjectRequest(getObjectRequest)
            .build();

        // 使用 AWS S3 预签名 URL 的生成器 获取对象的预签名 URL
        URL url = presigner.presignGetObject(getObjectPresignRequest).url();
        return url.toString();
    }

    /**
     * 上传 byte[] 数据到 Amazon S3，使用指定的后缀构造对象键。
     *
     * @param data   要上传的 byte[] 数据
     * @param suffix 对象键的后缀
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult uploadSuffix(byte[] data, String suffix) {
        return upload(new ByteArrayInputStream(data), getPath(properties.getPrefix(), suffix), Long.valueOf(data.length));
    }

    /**
     * 上传 InputStream 到 Amazon S3，使用指定的后缀构造对象键。
     *
     * @param inputStream 要上传的输入流
     * @param suffix      对象键的后缀
     * @param length      输入流的长度
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult uploadSuffix(InputStream inputStream, String suffix, Long length) {
        return upload(inputStream, getPath(properties.getPrefix(), suffix), length);
    }

    /**
     * 上传文件到 Amazon S3，使用指定的后缀构造对象键
     *
     * @param file   要上传的文件
     * @param suffix 对象键的后缀
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult uploadSuffix(File file, String suffix) {
        return upload(file.toPath(), getPath(properties.getPrefix(), suffix), null);
    }

    /**
     * 获取文件输入流
     *
     * @param path 完整文件路径
     * @return 输入流
     */
    public InputStream getObjectContent(String path) throws IOException {
        // 下载文件到临时目录
        Path tempFilePath = fileDownload(path);
        // 创建输入流
        InputStream inputStream = Files.newInputStream(tempFilePath);
        // 删除临时文件
        FileUtils.del(tempFilePath);
        // 返回对象内容的输入流
        return inputStream;
    }

    /**
     * 获取 S3 客户端的终端点 URL
     *
     * @return 终端点 URL
     */
    public String getEndpoint() {
        // 根据配置文件中的是否使用 HTTPS，设置协议头部
        String header = getIsHttps();
        // 拼接协议头部和终端点，得到完整的终端点 URL
        return header + properties.getEndpoint();
    }

    /**
     * 获取 S3 客户端的终端点 URL（自定义域名）
     *
     * @return 终端点 URL
     */
    public String getDomain() {
        // 从配置中获取域名、终端点、是否使用 HTTPS 等信息
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        String header = getIsHttps();
        // 云服务商直接返回
        if (StringUtils.containsAny(endpoint, OssConstant.CLOUD_SERVICE)) {
            if (StringUtils.isNotBlank(domain)) {
                return header + domain;
            }
            return header + endpoint;
        }
        // minio 单独处理
        if (StringUtils.isNotBlank(domain)) {
            // 如果 domain 以 "https://" 或 "http://" 开头
            if (domain.startsWith(Constants.HTTPS) || domain.startsWith(Constants.HTTP)) {
                return domain;
            }
            return header + domain;
        }
        return header + endpoint;
    }

    /**
     * 根据传入的 region 参数返回相应的 AWS 区域
     * 如果 region 参数非空，使用 Region.of 方法创建并返回对应的 AWS 区域对象
     * 如果 region 参数为空，返回一个默认的 AWS 区域（例如，us-east-1），作为广泛支持的区域
     *
     * @return 对应的 AWS 区域对象，或者默认的广泛支持的区域（us-east-1）
     */
    public Region of() {
        //AWS 区域字符串
        String region = properties.getRegion();
        if (StringUtils.isNotEmpty(region)) {
            // 如果 region 参数非空，使用 Region.of 方法创建对应的 AWS 区域对象
            return Region.of(region);
        }
        // 如果 region 参数为空，返回一个默认的 AWS 区域（例如，us-east-1），作为广泛支持的区域
        return Region.US_EAST_1;
    }

    /**
     * 获取云存储服务的URL
     *
     * @return 文件路径
     */
    public String getUrl() {
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        String header = getIsHttps();
        // 云服务商直接返回
        if (StringUtils.containsAny(endpoint, OssConstant.CLOUD_SERVICE)) {
            if (StringUtils.isNotBlank(domain)) {
                return header + domain;
            }
            return header + properties.getBucketName() + "." + endpoint;
        }
        // minio 单独处理
        if (StringUtils.isNotBlank(domain)) {
            // 如果 domain 以 "https://" 或 "http://" 开头
            if (domain.startsWith(Constants.HTTPS) || domain.startsWith(Constants.HTTP)) {
                // 解决自定义域名和站点协议不一致问题）
                return domain + "/" + properties.getBucketName();
            }
            return header + domain + "/" + properties.getBucketName();
        }
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
        if (StringUtils.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }
        return path + suffix;
    }

    /**
     * 移除路径中的基础URL部分，得到相对路径
     *
     * @param path 完整的路径，包括基础URL和相对路径
     * @return 去除基础URL后的相对路径
     */
    public String removeBaseUrl(String path) {
        return path.replace(getUrl() + "/", "");
    }

    /**
     * 从给定的路径中提取文件名
     * 如果在路径中找到文件名，则返回提取到的文件名
     * 如果没有找到文件名，则返回原始路径
     *
     * @param path 要提取文件名的路径
     * @return 提取到的文件名或原始路径（如果没有找到文件名）
     */
    public String extractFileName(String path) {
        // 定义匹配文件名的正则表达式
        String fileNameRegex = ".*/([^/]+)$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(fileNameRegex);
        // 创建匹配器
        Matcher matcher = pattern.matcher(path);
        // 查找匹配
        if (matcher.find()) {
            // 获取匹配的部分，即文件名
            return matcher.group(1);
        } else {
            // 如果没有匹配，返回原始字符串
            return path;
        }
    }

    /**
     * 服务商
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * 获取是否使用 HTTPS 的配置，并返回相应的协议头部。
     *
     * @return 协议头部，根据是否使用 HTTPS 返回 "https://" 或 "http://"
     */
    public String getIsHttps() {
        return OssConstant.IS_HTTPS.equals(properties.getIsHttps()) ? Constants.HTTPS : Constants.HTTP;
    }

    /**
     * 检查配置是否相同
     */
    public boolean checkPropertiesSame(OssProperties properties) {
        return this.properties.equals(properties);
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
     * 生成 AWS S3 存储桶访问策略
     *
     * @param bucketName 存储桶
     * @param policyType 桶策略类型
     * @return 符合 AWS S3 存储桶访问策略格式的字符串
     */
    private static String getPolicy(String bucketName, PolicyType policyType) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n\"Statement\": [\n{\n\"Action\": [\n");
        builder.append(switch (policyType) {
            case WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucketMultipartUploads\"\n";
            case READ_WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucket\",\n\"s3:ListBucketMultipartUploads\"\n";
            default -> "\"s3:GetBucketLocation\"\n";
        });
        builder.append("],\n\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n},\n");
        if (policyType == PolicyType.READ) {
            builder.append("{\n\"Action\": [\n\"s3:ListBucket\"\n],\n\"Effect\": \"Deny\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n},\n");
        }
        builder.append("{\n\"Action\": ");
        builder.append(switch (policyType) {
            case WRITE -> "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n";
            case READ_WRITE -> "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:GetObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n";
            default -> "\"s3:GetObject\",\n";
        });
        builder.append("\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("/*\"\n}\n],\n\"Version\": \"2012-10-17\"\n}\n");
        return builder.toString();
    }

}
