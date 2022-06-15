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
 * 沟通消息业务对象 user_communication_message
 *
 * @author huan.li
 * @date 2022-06-15
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("沟通消息业务对象")
public class UserCommunicationMessageBo extends BaseEntity {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", required = true)
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 发送消息用户
     */
    @ApiModelProperty(value = "发送消息用户", required = true)
    @NotNull(message = "发送消息用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sendMessageId;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String messageContent;

    /**
     * 是否由我发送
     */
    @ApiModelProperty(value = "是否由我发送", required = true)
    @NotNull(message = "是否由我发送不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer sendFromMeStatus;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值", required = true)
    @NotBlank(message = "搜索值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String searchValue;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除", required = true)
    @NotNull(message = "逻辑删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;


}
