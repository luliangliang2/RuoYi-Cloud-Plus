package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizRadar;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 上装雷达视图对象 biz_radar
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizRadar.class)
public class BizRadarVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 雷达ID
     */
    @ExcelProperty(value = "雷达ID")
    private Long radarId;

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
     * 雷达编码
     */
    @ExcelProperty(value = "雷达编码")
    private String radarCode;

    /**
     * 雷达名称
     */
    @ExcelProperty(value = "雷达名称")
    private String radarName;

    /**
     * 设备SN号
     */
    @ExcelProperty(value = "设备SN号")
    private String sn;

    /**
     * 雷达线数
     */
    @ExcelProperty(value = "雷达线数")
    private Integer lineCount;

    /**
     * 探测范围（米）
     */
    @ExcelProperty(value = "探测范围（米）")
    private BigDecimal detectionRange;

    /**
     * 厂商
     */
    @ExcelProperty(value = "厂商")
    private String manufacturer;

    /**
     * 型号
     */
    @ExcelProperty(value = "型号")
    private String modelName;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 是否已绑定车辆
     */
    private Boolean bound;

    /**
     * 绑定车辆ID
     */
    private Long boundVehicleId;

    /**
     * 绑定车辆车牌
     */
    private String boundPlateNo;

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
