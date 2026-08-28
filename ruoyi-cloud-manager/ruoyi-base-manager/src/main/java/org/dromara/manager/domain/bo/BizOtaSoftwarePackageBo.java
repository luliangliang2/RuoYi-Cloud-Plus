package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizOtaSoftwarePackage;

/**
 * OTA软件包业务对象 biz_ota_software_package
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizOtaSoftwarePackage.class, reverseConvertGenerate = false)
public class BizOtaSoftwarePackageBo extends BaseEntity {

    /**
     * 软件包ID
     */
    @NotNull(message = "软件包ID不能为空", groups = { EditGroup.class })
    private Long packageId;

    /**
     * 软件包名称
     */
    @NotBlank(message = "软件包名称不能为空")
    @Size(max = 100, message = "软件包名称长度不能超过{max}个字符")
    private String packageName;

    /**
     * 版本号
     */
    @NotBlank(message = "版本号不能为空")
    @Size(max = 64, message = "版本号长度不能超过{max}个字符")
    private String version;

    /**
     * 软件包说明
     */
    @Size(max = 1000, message = "软件包说明长度不能超过{max}个字符")
    private String packageDesc;

    /**
     * 文件OSS ID
     */
    @NotBlank(message = "请上传软件包文件")
    @Size(max = 64, message = "文件OSS ID长度不能超过{max}个字符")
    private String fileOssId;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
