package org.dromara.common.netty.websocket.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Netty WebSocket 跨节点消息。
 *
 * @author ruoyi
 */
@Data
public class NettyWebSocketMessageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String path;

    private String tenantId;

    private Long userId;

    private List<String> sessionIds;

    private List<String> aliases;

    private String bizType;

    private String bizId;

    private String message;

    private boolean broadcast;
}
