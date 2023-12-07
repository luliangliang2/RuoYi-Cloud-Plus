package org.dromara.common.oss.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 上传任务返回体
 *
 * @author Feng
 */
@Data
@Builder
public class MultipartUploadResult {

    /**
     * 新分段上传的唯一 ID
     */
    private String uploadId;

    /**
     * 文件名
     */
    private String filename;
}
