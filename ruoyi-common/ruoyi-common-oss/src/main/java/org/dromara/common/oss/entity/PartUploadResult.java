package org.dromara.common.oss.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 上传部件返回体
 *
 * @author Feng
 */
@Data
@Builder
public class PartUploadResult {

    /**
     * 新上传部件的部件号
     */
    private Integer partNumber;

    /**
     * 从上传部分的内容生成的实体标签
     */
    private String eTag;
}
