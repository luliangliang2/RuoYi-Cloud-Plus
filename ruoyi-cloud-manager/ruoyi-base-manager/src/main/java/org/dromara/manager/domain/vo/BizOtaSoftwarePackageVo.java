package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizOtaSoftwarePackage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * OTA软件包视图对象 biz_ota_software_package
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizOtaSoftwarePackage.class)
public class BizOtaSoftwarePackageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 软件包ID
     */
    @ExcelProperty(value = "软件包ID")
    private Long packageId;

    /**
     * 软件包名称
     */
    @ExcelProperty(value = "软件包名称")
    private String packageName;

    /**
     * 版本号
     */
    @ExcelProperty(value = "版本号")
    private String version;

    /**
     * 软件包说明
     */
    @ExcelProperty(value = "软件包说明")
    private String packageDesc;

    /**
     * 文件OSS ID
     */
    @ExcelProperty(value = "文件OSS ID")
    private String fileOssId;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
