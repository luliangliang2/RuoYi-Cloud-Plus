package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizSceneRoute;

/**
 * 场景路线业务对象 biz_scene_route
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizSceneRoute.class, reverseConvertGenerate = false)
public class BizSceneRouteBo extends BaseEntity {

    /**
     * 路线ID
     */
    @NotNull(message = "路线ID不能为空", groups = { EditGroup.class })
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
     * 路线名称
     */
    @NotBlank(message = "路线名称不能为空")
    @Size(max = 100, message = "路线名称长度不能超过{max}个字符")
    private String routeName;

    /**
     * 高德GCJ02坐标路线JSON
     */
    @NotBlank(message = "路线范围不能为空")
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
    @Size(max = 32, message = "线颜色长度不能超过{max}个字符")
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
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
