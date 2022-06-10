package com.project.system.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 人脉职业标签业务对象 user_occupation_label
 *
 * @author project
 * @date 2022-06-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("人脉职业标签业务对象")
public class UserOccupationLabelBo extends BaseEntity {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", required = true)
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 代表当前用户;用户信息ID
     */
    @ApiModelProperty(value = "代表当前用户;用户信息ID", required = true)
    @NotNull(message = "代表当前用户;用户信息ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userInfoId;

    /**
     * 职业标签
     */
    @ApiModelProperty(value = "职业标签", required = true)
    @NotBlank(message = "职业标签不能为空", groups = { AddGroup.class, EditGroup.class })
    private String occupationLabel;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值", required = true)
    @NotBlank(message = "搜索值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String searchValue;

    /**
     * 逻辑删除;0->未删除，1->删除
     */
    @ApiModelProperty(value = "逻辑删除;0->未删除，1->删除", required = true)
    @NotNull(message = "逻辑删除;0->未删除，1->删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;


}
