package com.project.admin.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包交易记录业务对象 wallet_record
 *
 * @author project
 * @date 2022-06-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("钱包交易记录业务对象")
public class WalletRecordBo extends BaseEntity {

    /**
     * 自增ID
     */
    @ApiModelProperty(value = "自增ID", required = true)
    @NotNull(message = "自增ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 交易流水
     */
    @ApiModelProperty(value = "交易流水", required = true)
    @NotBlank(message = "交易流水不能为空", groups = { AddGroup.class, EditGroup.class })
    private String number;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式", required = true)
    @NotNull(message = "支付方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long fromId;

    /**
     * 接收方
     */
    @ApiModelProperty(value = "接收方", required = true)
    @NotNull(message = "接收方不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long toId;

    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型", required = true)
    @NotNull(message = "交易类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer type;

    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额", required = true)
    @NotNull(message = "交易金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal money;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式", required = true)
    @NotNull(message = "支付方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer payType;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", required = true)
    @NotBlank(message = "备注信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 支付状态
     */
    @ApiModelProperty(value = "支付状态", required = true)
    @NotNull(message = "支付状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer payStatus;

    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间", required = true)
    @NotNull(message = "交易时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date payTime;

    /**
     * 收款状态
     */
    @ApiModelProperty(value = "收款状态", required = true)
    @NotNull(message = "收款状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer fetchStatus;

    /**
     * 收款时间
     */
    @ApiModelProperty(value = "收款时间", required = true)
    @NotNull(message = "收款时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date fetchTime;

    /**
     * 对账状态
     */
    @ApiModelProperty(value = "对账状态", required = true)
    @NotNull(message = "对账状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer checkStatus;

    /**
     * 对账时间
     */
    @ApiModelProperty(value = "对账时间", required = true)
    @NotNull(message = "对账时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date checkTime;

    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态", required = true)
    @NotNull(message = "启用状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值", required = true)
    @NotBlank(message = "搜索值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String searchValue;


}
