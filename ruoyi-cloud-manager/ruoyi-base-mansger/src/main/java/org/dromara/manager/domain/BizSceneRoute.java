package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 场景路线对象 biz_scene_route
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_scene_route")
public class BizSceneRoute extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 路线ID
     */
    @TableId(value = "route_id")
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
