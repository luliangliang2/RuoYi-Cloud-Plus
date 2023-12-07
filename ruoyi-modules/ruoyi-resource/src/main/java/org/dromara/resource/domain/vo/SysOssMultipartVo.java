package org.dromara.resource.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分片上传对象信息
 *
 * @author Feng
 */
@Data
public class SysOssMultipartVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 新分段上传的唯一 ID
     */
    private String uploadId;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 新上传部件的部件号
     */
    private Integer partNumber;

    /**
     * 从上传部分的内容生成的实体标签
     */
    private String eTag;

    /**
     * 部分上传列表
     */
    private List<PartUploadResult> partUploadList;

    @Data
    @AllArgsConstructor
    public static class PartUploadResult implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 新上传部件的部件号
         */
        private Integer partNumber;

        /**
         * 从上传部分的内容生成的实体标签
         */
        private String eTag;
    }
}
