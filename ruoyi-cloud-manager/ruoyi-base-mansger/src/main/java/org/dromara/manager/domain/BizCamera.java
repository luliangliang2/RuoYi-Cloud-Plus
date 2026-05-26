package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
 * 上装相机对象 biz_camera
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_camera")
public class BizCamera extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 相机ID
     */
    @TableId(value = "camera_id")
    private Long cameraId;

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
     * 相机编码
     */
    private String cameraCode;

    /**
     * 相机名称
     */
    private String cameraName;

    /**
     * 设备SN号
     */
    private String sn;

    /**
     * 光角度数
     */
    private BigDecimal viewAngle;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String modelName;

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
