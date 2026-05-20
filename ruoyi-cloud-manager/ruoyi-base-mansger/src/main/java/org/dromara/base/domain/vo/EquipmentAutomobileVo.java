package org.dromara.base.domain.vo;

import org.dromara.base.domain.EquipmentAutomobile;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 车辆管理视图对象 equipment_automobile
 *
 * @author 路亮亮
 * @date 2026-03-18
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = EquipmentAutomobile.class)
public class EquipmentAutomobileVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * vin码
     */
    @ExcelProperty(value = "vin码")
    private String vin;

    /**
     * 车辆型号
     */
    @ExcelProperty(value = "车辆型号")
    private String brand;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String plateNumber;


}
