package com.project.contact.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 对我感兴趣业务对象 user_interested_to_me
 *
 * @author huan.li
 * @date 2022-06-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("对我感兴趣业务对象")
public class UserInterestedToMeBo extends BaseEntity {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", required = true)
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 当前用户
     */
    @ApiModelProperty(value = "当前用户", required = true)
    @NotNull(message = "当前用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userInfoId;

    /**
     * 人脉用户
     */
    @ApiModelProperty(value = "人脉用户", required = true)
    @NotNull(message = "人脉用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long contactInfoId;

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
    @NotNull(message = "逻辑删除;0->未删除，1->删除不能为空", groups = { EditGroup.class })
    private Integer deleted;


}
