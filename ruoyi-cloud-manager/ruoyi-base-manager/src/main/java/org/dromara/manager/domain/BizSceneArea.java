package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.List;

/**
 * 场景区域对象 biz_scene_area
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_scene_area")
public class BizSceneArea extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 区域ID
     */
    @TableId(value = "area_id")
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
     * 分类节点ID集合（查询使用）
     */
    @TableField(exist = false)
    private List<Long> categoryNodeIds;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域类型
     */
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
