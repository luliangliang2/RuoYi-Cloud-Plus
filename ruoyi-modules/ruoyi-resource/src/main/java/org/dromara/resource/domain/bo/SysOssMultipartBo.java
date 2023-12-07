package org.dromara.resource.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.core.validate.QueryGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 分片上传对象信息
 *
 * @author Feng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOssMultipartBo extends BaseEntity {

    /**
     * 分段上传的 ID
     */
    @NotBlank(message = "分段上传的ID不能为空", groups = {AddGroup.class, EditGroup.class, QueryGroup.class})
    @Size(min = 5, max = 255, message = "分段上传的ID长度必须在5到255之间")
    private String uploadId;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空", groups = {AddGroup.class, EditGroup.class, QueryGroup.class})
    @Size(min = 1, max = 255, message = "文件名长度必须在1到255之间")
    private String filename;

    /**
     * 文件原名
     */
    @NotBlank(message = "文件原名不能为空", groups = {EditGroup.class})
    @Size(min = 1, max = 255, message = "文件原名长度必须在1到255之间")
    private String originalfileName;

    /**
     * 描述此分段相对于分段上传中其他分段的位置的分段编号。部件号必须介于 1 和 10,000（含）之间。
     */
    @NotNull(message = "部件号不能为空", groups = {AddGroup.class})
    @Size(min = 1, max = 10000, message = "部件号必须介于1和10,000之间")
    private Integer partNumber;

    /**
     * 此部分的大小（以字节为单位）必须大于等于5M。（最后一段除外）
     */
    @NotNull(message = "此部分的大小不能为空", groups = {AddGroup.class})
    private Long partSize;

    /**
     * MD5哈希值验证数据完整性
     */
    @NotBlank(message = " MD5哈希值不能为空", groups = {AddGroup.class})
    @Size(min = 32, max = 32, message = "MD5哈希值长度必须为32")
    private String md5Digest;

}
