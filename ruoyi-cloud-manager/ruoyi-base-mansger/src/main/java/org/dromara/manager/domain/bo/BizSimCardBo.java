package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizSimCard;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SIM卡业务对象 biz_sim_card
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizSimCard.class, reverseConvertGenerate = false)
public class BizSimCardBo extends BaseEntity {

    /**
     * SIM卡ID
     */
    @NotNull(message = "SIM卡ID不能为空", groups = { EditGroup.class })
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
     * IMEI
     */
    @Size(max = 32, message = "IMEI长度不能超过{max}个字符")
    private String imei;

    /**
     * ICCID
     */
    @NotBlank(message = "ICCID不能为空")
    @Size(max = 32, message = "ICCID长度不能超过{max}个字符")
    private String iccid;

    /**
     * 手机号
     */
    @Size(max = 32, message = "手机号长度不能超过{max}个字符")
    private String phoneNumber;

    /**
     * 月套餐流量（GB）
     */
    @DecimalMin(value = "0", message = "月套餐流量不能小于0")
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
    @DecimalMin(value = "0", message = "当前用量不能小于0")
    private BigDecimal currentDataUsage;

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
