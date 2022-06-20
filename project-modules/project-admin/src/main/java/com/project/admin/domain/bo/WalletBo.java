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
 * 用户钱包业务对象 wallet
 *
 * @author project
 * @date 2022-06-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户钱包业务对象")
public class WalletBo extends BaseEntity {

    /**
     * 钱包id
     */
    @ApiModelProperty(value = "钱包id", required = true)
    @NotNull(message = "钱包id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 总余额
     */
    @ApiModelProperty(value = "总余额", required = true)
    @NotNull(message = "总余额不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long balance;

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
