package org.dromara.common.oss.entity;

import lombok.Builder;
import lombok.Data;
import org.dromara.common.core.utils.StringUtils;

/**
 * 上传返回体
 *
 * @author Lion Li
 */
@Data
@Builder
public class UploadResult {

    /**
     * 文件路径
     */
    private String url;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 已上传对象的实体标记（用来校验文件）
     */
    private String eTag;

    public String getETag() {
        if (StringUtils.isBlank(this.eTag)) {
            return this.eTag;
        }
        return this.eTag.replaceAll("\"", "");
    }

}
