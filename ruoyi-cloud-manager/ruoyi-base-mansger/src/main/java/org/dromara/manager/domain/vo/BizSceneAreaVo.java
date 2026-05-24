package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizSceneArea;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 场景区域视图对象 biz_scene_area
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizSceneArea.class)
public class BizSceneAreaVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 区域ID
     */
    @ExcelProperty(value = "区域ID")
    private Long areaId;

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
     * 区域名称
     */
    @ExcelProperty(value = "区域名称")
    private String areaName;

    /**
     * 区域类型
     */
    @ExcelProperty(value = "区域类型")
    private String areaType;

    /**
     * 高德GCJ02坐标范围JSON
     */
    private String gcj02Path;

    /**
     * 百度BD09坐标范围JSON
     */
    private String bd09Path;

    /**
     * WGS84坐标范围JSON
     */
    private String wgs84Path;

    /**
     * 填充颜色
     */
    private String fillColor;

    /**
     * 边界线颜色
     */
    private String strokeColor;

    /**
     * 边界线样式（solid实线 dashed虚线）
     */
    private String strokeStyle;

    /**
     * 边界线宽度
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
