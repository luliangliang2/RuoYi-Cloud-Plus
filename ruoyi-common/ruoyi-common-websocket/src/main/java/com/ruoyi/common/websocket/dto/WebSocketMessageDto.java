package com.ruoyi.common.websocket.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 消息的dto
 * </p>
 *
 * @author lyt
 * @since 2022/11/3
 */
@Data
public class WebSocketMessageDto implements Serializable {
    /**
     * 需要推送到的用户ID
     */
    private Long userId;
    /**
     * 需要发送的消息
     */
    private Object message;

}
