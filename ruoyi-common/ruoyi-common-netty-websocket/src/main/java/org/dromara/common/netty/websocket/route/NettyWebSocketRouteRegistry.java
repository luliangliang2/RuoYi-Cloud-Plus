package org.dromara.common.netty.websocket.route;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 路由注册表。
 *
 * @author ruoyi
 */
public class NettyWebSocketRouteRegistry {

    private final Map<String, NettyWebSocketRoute> routes = new ConcurrentHashMap<>();

    public NettyWebSocketRoute route(String path) {
        if (StrUtil.isBlank(path)) {
            throw new IllegalArgumentException("path must not be blank");
        }
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return routes.computeIfAbsent(normalizedPath, NettyWebSocketRoute::new);
    }

    public NettyWebSocketRoute get(String path) {
        return routes.get(path);
    }
}
