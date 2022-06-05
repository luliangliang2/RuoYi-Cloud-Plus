package com.project.admin.contact.domain.bo;

import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 沟通消息业务对象 user_communication_message
 *
 * @author project
 * @date 2022-06-05
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("沟通消息业务对象")
public class UserCommunicationMessageBo extends BaseEntity {

    /**
     * 逻辑删除;0未删除1已删除
     */
    @ApiModelProperty(value = "逻辑删除;0未删除1已删除", required = true)
    @NotNull(message = "逻辑删除;0未删除1已删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 代表当前用户
     */
    @ApiModelProperty(value = "代表当前用户", required = true)
    @NotNull(message = "代表当前用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userInfoId;

    /**
     * 代表人脉用户
     */
    @ApiModelProperty(value = "代表人脉用户", required = true)
    @NotNull(message = "代表人脉用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long contactInfoId;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String messageContent;


}
