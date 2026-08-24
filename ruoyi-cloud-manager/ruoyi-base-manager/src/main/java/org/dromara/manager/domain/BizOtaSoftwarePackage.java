package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * OTA软件包对象 biz_ota_software_package
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_ota_software_package")
public class BizOtaSoftwarePackage extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 软件包ID
     */
    @TableId(value = "package_id")
    private Long packageId;

    /**
     * 软件包名称
     */
    private String packageName;

    /**
     * 版本号
     */
    private String version;

    /**
     * 软件包说明
     */
    private String packageDesc;

    /**
     * 文件OSS ID
     */
    private String fileOssId;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

}
