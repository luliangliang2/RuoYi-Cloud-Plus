package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 车辆上装绑定对象 biz_vehicle_equipment_bind
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_vehicle_equipment_bind")
public class BizVehicleEquipmentBind extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 绑定ID
     */
    @TableId(value = "bind_id")
    private Long bindId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 设备类型（1相机 2雷达）
     */
    private String equipmentType;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 安装位置
     */
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
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

}
