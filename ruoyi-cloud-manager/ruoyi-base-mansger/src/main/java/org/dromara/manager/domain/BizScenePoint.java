package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 场景点位对象 biz_scene_point
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_scene_point")
public class BizScenePoint extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 点位ID
     */
    @TableId(value = "point_id")
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
     * 路线ID
     */
    private Long routeId;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 高德GCJ02经度
     */
    private BigDecimal gcj02Lng;

    /**
     * 高德GCJ02纬度
     */
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
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactPhone;

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
