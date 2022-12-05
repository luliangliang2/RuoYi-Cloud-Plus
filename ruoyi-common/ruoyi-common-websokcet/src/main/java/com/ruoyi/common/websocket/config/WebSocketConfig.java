package com.ruoyi.common.websocket.config;

import com.ruoyi.common.websocket.handler.PlusWebSocketHandler;
import com.ruoyi.common.websocket.interceptor.PlusWebSocketInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * <p>
 * WebSocket配置
 * </p>
 *
 * @author lyt
 * @since 2022/10/31
 */
@AutoConfiguration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( new PlusWebSocketHandler(), "/ws" )
            .addInterceptors( new PlusWebSocketInterceptor() )
            .setAllowedOrigins( "*" );
    }

}
