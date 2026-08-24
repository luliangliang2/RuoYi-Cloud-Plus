package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizVehicleEquipmentBind;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 车辆上装绑定视图对象 biz_vehicle_equipment_bind
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizVehicleEquipmentBind.class)
public class BizVehicleEquipmentBindVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 绑定ID
     */
    @ExcelProperty(value = "绑定ID")
    private Long bindId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 设备类型（1相机 2雷达）
     */
    @ExcelProperty(value = "设备类型")
    private String equipmentType;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 设备编码
     */
    @ExcelProperty(value = "设备编码")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称")
    private String equipmentName;

    /**
     * 设备SN号
     */
    @ExcelProperty(value = "设备SN号")
    private String sn;

    /**
     * 安装位置
     */
    @ExcelProperty(value = "安装位置")
    private String installPosition;

    /**
     * 安装时间
     */
    @ExcelProperty(value = "安装时间")
    private Date installTime;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
