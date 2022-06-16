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
 * @date 2022-06-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("沟通消息业务对象")
public class UserCommunicationMessageBo extends BaseEntity {

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号", required = true)
    @NotNull(message = "序号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户序号
     */
    @ApiModelProperty(value = "用户序号", required = true)
    @NotNull(message = "用户序号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userInfoId;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String messageContent;

    /**
     * 消息状态
     */
    @ApiModelProperty(value = "消息状态", required = true)
    @NotNull(message = "消息状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer messageStatus;

    /**
     * 搜索值
     */
    @ApiModelProperty(value = "搜索值", required = true)
    @NotBlank(message = "搜索值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String searchValue;

    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态", required = true)
    @NotNull(message = "启用状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;


}
