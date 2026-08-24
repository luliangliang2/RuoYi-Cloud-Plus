package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 告警视图对象
 *
 * @author LionLi
 * @date 2026-05-28
 */
@Data
@ExcelIgnoreUnannotated
public class BizAlarmVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 告警时间
     */
    @ExcelProperty(value = "告警时间")
    private Date ts;

    /**
     * VIN码
     */
    @ExcelProperty(value = "VIN码")
    private String vin;

    /**
     * 车辆类型
     */
    @ExcelProperty(value = "车辆类型")
    private String vehicleType;

    /**
     * 告警编码
     */
    @ExcelProperty(value = "告警编码")
    private Integer alarmCode;

    /**
     * 告警等级
     */
    @ExcelProperty(value = "告警等级")
    private Integer alarmLevel;

    /**
     * 告警描述
     */
    @ExcelProperty(value = "告警描述")
    private String alarmMsg;

    /**
     * 告警值
     */
    @ExcelProperty(value = "告警值")
    private Double val;

    /**
     * 是否已处理 0未处理 1已处理
     */
    @ExcelProperty(value = "是否已处理")
    private Integer handled;

}
