package com.project.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.project.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 沟通消息对象 user_communication_message
 *
 * @author huan.li
 * @date 2022-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_communication_message")
public class UserCommunicationMessage extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * ID
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 发送消息用户
     */
    private Long sendMessageId;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 是否由我发送
     */
    private Integer sendFromMeStatus;
    /**
     * 搜索值
     */
    private String searchValue;
    /**
     * 逻辑删除
     */
    private Integer deleted;

}
