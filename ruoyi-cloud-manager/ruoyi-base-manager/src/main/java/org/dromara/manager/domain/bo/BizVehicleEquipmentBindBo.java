package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizVehicleEquipmentBind;

import java.util.Date;

/**
 * 车辆上装绑定业务对象 biz_vehicle_equipment_bind
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizVehicleEquipmentBind.class, reverseConvertGenerate = false)
public class BizVehicleEquipmentBindBo extends BaseEntity {

    /**
     * 绑定ID
     */
    @NotNull(message = "绑定ID不能为空", groups = { EditGroup.class })
    private Long bindId;

    /**
     * 车辆ID
     */
    @NotNull(message = "车辆ID不能为空")
    private Long vehicleId;

    /**
     * 设备类型（1相机 2雷达）
     */
    @NotBlank(message = "设备类型不能为空")
    @Pattern(regexp = "^[12]$", message = "设备类型不正确")
    private String equipmentType;

    /**
     * 设备ID
     */
    @NotNull(message = "设备不能为空")
    private Long equipmentId;

    /**
     * 安装位置
     */
    @Size(max = 64, message = "安装位置长度不能超过{max}个字符")
    private String installPosition;

    /**
     * 安装时间
     */
    private Date installTime;

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
