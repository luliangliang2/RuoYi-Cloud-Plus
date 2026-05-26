package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizScenePoint;

import java.math.BigDecimal;
import java.util.List;

/**
 * 场景点位业务对象 biz_scene_point
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizScenePoint.class, reverseConvertGenerate = false)
public class BizScenePointBo extends BaseEntity {

    /**
     * 点位ID
     */
    @NotNull(message = "点位ID不能为空", groups = { EditGroup.class })
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
     * 分类节点ID集合
     */
    private List<Long> categoryNodeIds;

    /**
     * 路线ID
     */
    @NotNull(message = "路线不能为空")
    private Long routeId;

    /**
     * 点位名称
     */
    @NotBlank(message = "点位名称不能为空")
    @Size(max = 100, message = "点位名称长度不能超过{max}个字符")
    private String pointName;

    /**
     * 高德GCJ02经度
     */
    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180", message = "经度不能小于-180")
    @DecimalMax(value = "180", message = "经度不能大于180")
    private BigDecimal gcj02Lng;

    /**
     * 高德GCJ02纬度
     */
    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90", message = "纬度不能小于-90")
    @DecimalMax(value = "90", message = "纬度不能大于90")
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
    @Size(max = 50, message = "联系人长度不能超过{max}个字符")
    private String contactName;

    /**
     * 联系人手机号
     */
    @Size(max = 20, message = "联系人手机号长度不能超过{max}个字符")
    private String contactPhone;

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
