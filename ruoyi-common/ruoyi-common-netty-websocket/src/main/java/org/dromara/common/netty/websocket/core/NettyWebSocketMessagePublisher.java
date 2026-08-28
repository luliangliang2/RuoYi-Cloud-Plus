package org.dromara.common.netty.websocket.core;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.netty.websocket.config.properties.NettyWebSocketProperties;
import org.dromara.common.netty.websocket.dto.NettyWebSocketMessageDto;
import org.dromara.common.netty.websocket.session.NettyWebSocketSessionManager;
import org.dromara.common.redis.utils.RedisUtils;

import java.util.List;

/**
 * Netty WebSocket 消息发布器。
 *
 * @author ruoyi
 */
@Slf4j
public class NettyWebSocketMessagePublisher {

    private final NettyWebSocketProperties properties;

    private final NettyWebSocketSessionManager sessionManager;

    public NettyWebSocketMessagePublisher(NettyWebSocketProperties properties, NettyWebSocketSessionManager sessionManager) {
        this.properties = properties;
        this.sessionManager = sessionManager;
    }

    public int sendBySessionId(String sessionId, String message) {
        return sessionManager.sendBySessionId(sessionId, message);
    }

    public int sendByPath(String path, String message) {
        return sessionManager.sendByPath(path, message);
    }

    public int sendByTenant(String tenantId, String message) {
        return sessionManager.sendByTenant(tenantId, message);
    }

    public int sendByUser(Long userId, String message) {
        return sessionManager.sendByUser(userId, message);
    }

    public int sendByAlias(String alias, String message) {
        return sessionManager.sendByAlias(alias, message);
    }

    public int sendByAliases(List<String> aliases, String message) {
        if (CollUtil.isEmpty(aliases)) {
            return 0;
        }
        return aliases.stream().mapToInt(alias -> sendByAlias(alias, message)).sum();
    }

    public int sendByBiz(String bizType, String bizId, String message) {
        return sessionManager.sendByBiz(bizType, bizId, message);
    }

    public int sendByPathAndTenant(String path, String tenantId, String message) {
        return sessionManager.sendByPathAndTenant(path, tenantId, message);
    }

    public int sendByPathAndUser(String path, Long userId, String message) {
        return sessionManager.sendByPathAndUser(path, userId, message);
    }

    public int sendByPathAndAlias(String path, String alias, String message) {
        return sessionManager.sendByPathAndAlias(path, alias, message);
    }

    public int sendByPathAndAliases(String path, List<String> aliases, String message) {
        if (CollUtil.isEmpty(aliases)) {
            return 0;
        }
        return aliases.stream().mapToInt(alias -> sendByPathAndAlias(path, alias, message)).sum();
    }

    public int sendByPathAndBiz(String path, String bizType, String bizId, String message) {
        return sessionManager.sendByPathAndBiz(path, bizType, bizId, message);
    }

    public int broadcast(String message) {
        return sessionManager.broadcast(message);
    }

    public void publish(NettyWebSocketMessageDto messageDto) {
        RedisUtils.publish(properties.getRedisTopic(), messageDto, consumer ->
            log.info("Netty WebSocket publish topic:{} message:{}", properties.getRedisTopic(), messageDto));
    }

    public void handle(NettyWebSocketMessageDto messageDto) {
        if (messageDto == null) {
            return;
        }
        String message = messageDto.getMessage();
        if (messageDto.isBroadcast()) {
            broadcast(message);
        }
        if (messageDto.getPath() != null) {
            handlePathMessage(messageDto, message);
            return;
        }
        if (messageDto.getTenantId() != null) {
            sendByTenant(messageDto.getTenantId(), message);
        }
        if (messageDto.getUserId() != null) {
            sendByUser(messageDto.getUserId(), message);
        }
        if (CollUtil.isNotEmpty(messageDto.getSessionIds())) {
            sessionManager.sendBySessionIds(messageDto.getSessionIds(), message);
        }
        if (CollUtil.isNotEmpty(messageDto.getAliases())) {
            sendByAliases(messageDto.getAliases(), message);
        }
        if (messageDto.getBizType() != null && messageDto.getBizId() != null) {
            sendByBiz(messageDto.getBizType(), messageDto.getBizId(), message);
        }
    }

    private void handlePathMessage(NettyWebSocketMessageDto messageDto, String message) {
        boolean hasTarget = false;
        String path = messageDto.getPath();
        if (messageDto.getTenantId() != null) {
            hasTarget = true;
            sendByPathAndTenant(path, messageDto.getTenantId(), message);
        }
        if (messageDto.getUserId() != null) {
            hasTarget = true;
            sendByPathAndUser(path, messageDto.getUserId(), message);
        }
        if (CollUtil.isNotEmpty(messageDto.getSessionIds())) {
            hasTarget = true;
            sessionManager.sendBySessionIds(messageDto.getSessionIds(), message);
        }
        if (CollUtil.isNotEmpty(messageDto.getAliases())) {
            hasTarget = true;
            sendByPathAndAliases(path, messageDto.getAliases(), message);
        }
        if (messageDto.getBizType() != null && messageDto.getBizId() != null) {
            hasTarget = true;
            sendByPathAndBiz(path, messageDto.getBizType(), messageDto.getBizId(), message);
        }
        if (!hasTarget) {
            sendByPath(path, message);
        }
    }
}
