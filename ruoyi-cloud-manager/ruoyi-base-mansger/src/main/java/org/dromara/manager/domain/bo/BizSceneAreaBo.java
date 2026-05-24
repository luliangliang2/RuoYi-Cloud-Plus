package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizSceneArea;

/**
 * 场景区域业务对象 biz_scene_area
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizSceneArea.class, reverseConvertGenerate = false)
public class BizSceneAreaBo extends BaseEntity {

    /**
     * 区域ID
     */
    @NotNull(message = "区域ID不能为空", groups = { EditGroup.class })
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
     * 区域名称
     */
    @NotBlank(message = "区域名称不能为空")
    @Size(max = 100, message = "区域名称长度不能超过{max}个字符")
    private String areaName;

    /**
     * 区域类型
     */
    @NotBlank(message = "区域类型不能为空")
    @Size(max = 32, message = "区域类型长度不能超过{max}个字符")
    private String areaType;

    /**
     * 高德GCJ02坐标范围JSON
     */
    @NotBlank(message = "场景范围不能为空")
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
    @Size(max = 32, message = "填充颜色长度不能超过{max}个字符")
    private String fillColor;

    /**
     * 边界线颜色
     */
    @Size(max = 32, message = "边界线颜色长度不能超过{max}个字符")
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
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
