package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizScenePoint;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 场景点位视图对象 biz_scene_point
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizScenePoint.class)
public class BizScenePointVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 点位ID
     */
    @ExcelProperty(value = "点位ID")
    private Long pointId;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long categoryNodeId;

    /**
     * 分类节点名称
     */
    @ExcelProperty(value = "分类")
    private String categoryNodeName;

    /**
     * 路线ID
     */
    private Long routeId;

    /**
     * 路线名称
     */
    @ExcelProperty(value = "路线")
    private String routeName;

    /**
     * 路线高德GCJ02坐标JSON
     */
    private String routeGcj02Path;

    /**
     * 点位名称
     */
    @ExcelProperty(value = "点位名称")
    private String pointName;

    /**
     * 高德GCJ02经度
     */
    @ExcelProperty(value = "经度")
    private BigDecimal gcj02Lng;

    /**
     * 高德GCJ02纬度
     */
    @ExcelProperty(value = "纬度")
    private BigDecimal gcj02Lat;

    /**
     * 百度BD09经度
     */
    private BigDecimal bd09Lng;

    /**
     * 百度BD09纬度
     */
    private BigDecimal bd09Lat;

    /**
     * WGS84经度
     */
    private BigDecimal wgs84Lng;

    /**
     * WGS84纬度
     */
    private BigDecimal wgs84Lat;

    /**
     * 联系人
     */
    @ExcelProperty(value = "联系人")
    private String contactName;

    /**
     * 联系人手机号
     */
    @ExcelProperty(value = "联系人手机号")
    private String contactPhone;

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
