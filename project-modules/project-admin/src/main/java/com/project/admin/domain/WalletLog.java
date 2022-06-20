package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 钱包变动日志对象 wallet_log
 *
 * @author project
 * @date 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wallet_log")
public class WalletLog extends BaseEntity {

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
     * 用户ID
     */
    private Long userId;
    /**
     * 变动金额
     */
    private Long beforeMoney;
    /**
     * 变动金额
     */
    private Long afterMoney;
    /**
     * 业务类型
     */
    private Integer targetType;
    /**
     * 目标id
     */
    private Long targetId;
    /**
     * 处理状态
     */
    private Integer status;
    /**
     * 处理结果
     */
    private Integer resultType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 启用状态
     */
    private Integer deleted;
    /**
     * 搜索值
     */
    private String searchValue;

}
