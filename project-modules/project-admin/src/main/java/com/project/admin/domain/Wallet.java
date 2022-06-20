package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户钱包对象 wallet
 *
 * @author project
 * @date 2022-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wallet")
public class Wallet extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 钱包id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 总余额
     */
    private Long balance;
    /**
     * 启用状态
     */
    private Integer deleted;
    /**
     * 搜索值
     */
    private String searchValue;

}
