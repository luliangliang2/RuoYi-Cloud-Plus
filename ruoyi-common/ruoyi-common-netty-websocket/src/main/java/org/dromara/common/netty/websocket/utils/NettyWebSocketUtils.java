package org.dromara.common.netty.websocket.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.netty.websocket.core.NettyWebSocketMessagePublisher;
import org.dromara.common.netty.websocket.route.NettyWebSocketRoute;
import org.dromara.common.netty.websocket.route.NettyWebSocketRouteRegistry;
import org.dromara.common.netty.websocket.session.NettyWebSocketSessionManager;

import java.util.Map;

/**
 * Netty WebSocket 工具。
 *
 * @author ruoyi
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NettyWebSocketUtils {

    public static NettyWebSocketRoute route(String path) {
        return SpringUtils.getBean(NettyWebSocketRouteRegistry.class).route(path);
    }

    public static int sendByAlias(String alias, String message) {
        return SpringUtils.getBean(NettyWebSocketMessagePublisher.class).sendByAlias(alias, message);
    }

    public static int sendByBiz(String bizType, String bizId, String message) {
        return SpringUtils.getBean(NettyWebSocketMessagePublisher.class).sendByBiz(bizType, bizId, message);
    }

    public static int sendByPathAndAlias(String path, String alias, String message) {
        return SpringUtils.getBean(NettyWebSocketMessagePublisher.class).sendByPathAndAlias(path, alias, message);
    }

    public static int sendByPathAndBiz(String path, String bizType, String bizId, String message) {
        return SpringUtils.getBean(NettyWebSocketMessagePublisher.class).sendByPathAndBiz(path, bizType, bizId, message);
    }

    public static int sendByPath(String path, String message) {
        return SpringUtils.getBean(NettyWebSocketMessagePublisher.class).sendByPath(path, message);
    }

    public static int sendByTenant(String tenantId, String message) {
        return SpringUtils.getBean(NettyWebSocketMessagePublisher.class).sendByTenant(tenantId, message);
    }

    public static int broadcast(String message) {
        return SpringUtils.getBean(NettyWebSocketMessagePublisher.class).broadcast(message);
    }

    public static Map<String, Object> stats() {
        return SpringUtils.getBean(NettyWebSocketSessionManager.class).stats();
    }
}
