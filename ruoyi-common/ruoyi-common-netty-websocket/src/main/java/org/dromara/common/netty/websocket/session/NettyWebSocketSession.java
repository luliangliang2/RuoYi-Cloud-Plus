package org.dromara.common.netty.websocket.session;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import org.dromara.system.api.model.LoginUser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty WebSocket 会话。
 *
 * @author ruoyi
 */
@Data
@Builder
public class NettyWebSocketSession {

    private String sessionId;

    private String path;

    private String tenantId;

    private Long userId;

    private String username;

    private String alias;

    private String bizType;

    private String bizId;

    private String token;

    private LoginUser loginUser;

    private Channel channel;

    private long connectTime;

    private long lastActiveTime;

    @Builder.Default
    private Map<String, String> params = new ConcurrentHashMap<>();

    @Builder.Default
    private Map<String, Object> attrs = new ConcurrentHashMap<>();

    public boolean writable() {
        return channel != null && channel.isActive() && channel.isWritable();
    }
}
