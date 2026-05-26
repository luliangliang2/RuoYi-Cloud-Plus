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
import java.util.Date;
import java.util.List;

/**
 * SIM卡对象 biz_sim_card
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_sim_card")
public class BizSimCard extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * SIM卡ID
     */
    @TableId(value = "sim_id")
    private Long simId;

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
     * IMEI
     */
    private String imei;

    /**
     * ICCID
     */
    private String iccid;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 月套餐流量（GB）
     */
    private BigDecimal monthlyDataQuota;

    /**
     * 开卡时间
     */
    private Date activationTime;

    /**
     * 到期时间
     */
    private Date expireTime;

    /**
     * 当前用量（GB）
     */
    private BigDecimal currentDataUsage;

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
