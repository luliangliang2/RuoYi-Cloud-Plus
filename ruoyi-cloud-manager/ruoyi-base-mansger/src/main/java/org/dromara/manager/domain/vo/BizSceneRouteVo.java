package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizSceneRoute;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 场景路线视图对象 biz_scene_route
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizSceneRoute.class)
public class BizSceneRouteVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 路线ID
     */
    @ExcelProperty(value = "路线ID")
    private Long routeId;

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
     * 路线名称
     */
    @ExcelProperty(value = "路线名称")
    private String routeName;

    /**
     * 高德GCJ02坐标路线JSON
     */
    private String gcj02Path;

    /**
     * 百度BD09坐标路线JSON
     */
    private String bd09Path;

    /**
     * WGS84坐标路线JSON
     */
    private String wgs84Path;

    /**
     * 线颜色
     */
    private String strokeColor;

    /**
     * 线样式（solid实线 dashed虚线）
     */
    private String strokeStyle;

    /**
     * 线宽度
     */
    private Integer strokeWeight;

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
