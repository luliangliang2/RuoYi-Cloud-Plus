package org.dromara.manager.api.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.manager.api.domain.BizVehicle;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


/**
 * 车辆管理视图对象 biz_vehicle
 *
 * @author LionLi
 * @date 2026-05-21
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizVehicle.class)
public class BizVehicleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long categoryNodeId;

    /**
     * 分类节点ID集合
     */
    private List<Long> categoryNodeIds;

    /**
     * 分类节点名称
     */
    @ExcelProperty(value = "分类")
    private String categoryNodeName;

    /**
     * vin码
     */
    @ExcelProperty(value = "vin码")
    private String vin;

    /**
     * 车牌
     */
    @ExcelProperty(value = "车牌")
    private String plateNo;

    /**
     * 车辆品牌
     */
    @ExcelProperty(value = "车辆品牌", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "vehicle_brand")
    private String brand;

    /**
     * 是否在线
     */
    private Boolean online;


}
