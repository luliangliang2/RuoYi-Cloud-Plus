package com.project.admin.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 钱包变动日志业务对象 wallet_log
 *
 * @author project
 * @date 2022-06-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("钱包变动日志业务对象")
public class WalletLogBo extends BaseEntity {

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
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 变动金额
     */
    @ApiModelProperty(value = "变动金额", required = true)
    @NotNull(message = "变动金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long beforeMoney;

    /**
     * 变动金额
     */
    @ApiModelProperty(value = "变动金额", required = true)
    @NotNull(message = "变动金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long afterMoney;

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型", required = true)
    @NotNull(message = "业务类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer targetType;

    /**
     * 目标id
     */
    @ApiModelProperty(value = "目标id", required = true)
    @NotNull(message = "目标id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetId;

    /**
     * 处理状态
     */
    @ApiModelProperty(value = "处理状态", required = true)
    @NotNull(message = "处理状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 处理结果
     */
    @ApiModelProperty(value = "处理结果", required = true)
    @NotNull(message = "处理结果不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer resultType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

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
