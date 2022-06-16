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
 * 职业标签业务对象 user_occupation_label
 *
 * @author huan.li
 * @date 2022-06-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("职业标签业务对象")
public class UserOccupationLabelBo extends BaseEntity {

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号", required = true)
    @NotNull(message = "序号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 用户序号
     */
    @ApiModelProperty(value = "用户序号", required = true)
    @NotNull(message = "用户序号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userInfoId;

    /**
     * 职业标签
     */
    @ApiModelProperty(value = "职业标签", required = true)
    @NotBlank(message = "职业标签不能为空", groups = {AddGroup.class, EditGroup.class})
    private String occupationLabel;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值", required = true)
    @NotBlank(message = "搜索值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String searchValue;

    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态", required = true)
    @NotNull(message = "启用状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer deleted;


}
