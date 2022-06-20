package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包交易记录对象 wallet_record
 *
 * @author project
 * @date 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wallet_record")
public class WalletRecord extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 自增ID
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 交易流水
     */
    private String number;
    /**
     * 支付方式
     */
    private Long fromId;
    /**
     * 接收方
     */
    private Long toId;
    /**
     * 交易类型
     */
    private Integer type;
    /**
     * 交易金额
     */
    private BigDecimal money;
    /**
     * 支付方式
     */
    private Integer payType;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 交易时间
     */
    private Date payTime;
    /**
     * 收款状态
     */
    private Integer fetchStatus;
    /**
     * 收款时间
     */
    private Date fetchTime;
    /**
     * 对账状态
     */
    private Integer checkStatus;
    /**
     * 对账时间
     */
    private Date checkTime;
    /**
     * 启用状态
     */
    private Integer deleted;
    /**
     * 搜索值
     */
    private String searchValue;

}
