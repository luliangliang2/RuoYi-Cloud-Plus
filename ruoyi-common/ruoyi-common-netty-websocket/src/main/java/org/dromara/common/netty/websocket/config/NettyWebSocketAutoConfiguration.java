package org.dromara.common.netty.websocket.config;

import org.dromara.common.netty.websocket.config.properties.NettyWebSocketProperties;
import org.dromara.common.netty.websocket.core.NettyWebSocketMessagePublisher;
import org.dromara.common.netty.websocket.core.NettyWebSocketServer;
import org.dromara.common.netty.websocket.handler.NettyWebSocketAuthService;
import org.dromara.common.netty.websocket.listener.NettyWebSocketTopicListener;
import org.dromara.common.netty.websocket.route.NettyWebSocketRouteRegistry;
import org.dromara.common.netty.websocket.session.NettyWebSocketSessionManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Netty WebSocket 自动装配。
 *
 * @author ruoyi
 */
@AutoConfiguration
@ConditionalOnProperty(value = "netty-websocket.enabled", havingValue = "true")
@EnableConfigurationProperties(NettyWebSocketProperties.class)
public class NettyWebSocketAutoConfiguration {

    @Bean
    public NettyWebSocketSessionManager nettyWebSocketSessionManager() {
        return new NettyWebSocketSessionManager();
    }

    @Bean
    public NettyWebSocketRouteRegistry nettyWebSocketRouteRegistry() {
        return new NettyWebSocketRouteRegistry();
    }

    @Bean
    public NettyWebSocketAuthService nettyWebSocketAuthService(NettyWebSocketProperties properties) {
        return new NettyWebSocketAuthService(properties);
    }

    @Bean
    public NettyWebSocketMessagePublisher nettyWebSocketMessagePublisher(
        NettyWebSocketProperties properties,
        NettyWebSocketSessionManager sessionManager) {
        return new NettyWebSocketMessagePublisher(properties, sessionManager);
    }

    @Bean
    public NettyWebSocketTopicListener nettyWebSocketTopicListener(
        NettyWebSocketProperties properties,
        NettyWebSocketMessagePublisher publisher) {
        return new NettyWebSocketTopicListener(properties, publisher);
    }

    @Bean
    public NettyWebSocketServer nettyWebSocketServer(
        NettyWebSocketProperties properties,
        NettyWebSocketAuthService authService,
        NettyWebSocketSessionManager sessionManager,
        NettyWebSocketRouteRegistry routeRegistry) {
        return new NettyWebSocketServer(properties, authService, sessionManager, routeRegistry);
    }
}
