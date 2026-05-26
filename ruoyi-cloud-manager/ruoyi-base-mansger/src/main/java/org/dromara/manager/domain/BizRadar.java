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
 * 上装雷达对象 biz_radar
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_radar")
public class BizRadar extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 雷达ID
     */
    @TableId(value = "radar_id")
    private Long radarId;

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
     * 雷达编码
     */
    private String radarCode;

    /**
     * 雷达名称
     */
    private String radarName;

    /**
     * 设备SN号
     */
    private String sn;

    /**
     * 雷达线数
     */
    private Integer lineCount;

    /**
     * 探测范围（米）
     */
    private BigDecimal detectionRange;

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
