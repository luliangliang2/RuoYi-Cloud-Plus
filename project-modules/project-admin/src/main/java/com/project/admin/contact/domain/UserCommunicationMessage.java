package com.project.admin.contact.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 沟通消息对象 user_communication_message
 *
 * @author project
 * @date 2022-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_communication_message")
public class UserCommunicationMessage extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 逻辑删除;0未删除1已删除
     */
    private Integer deleted;
    /**
     * id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 代表当前用户
     */
    private Long userInfoId;
    /**
     * 代表人脉用户
     */
    private Long contactInfoId;
    /**
     * 消息内容
     */
    private String messageContent;

}
