package org.dromara.common.netty.websocket.route;

import lombok.Getter;
import org.dromara.common.netty.websocket.session.NettyWebSocketSession;

import java.util.concurrent.atomic.AtomicReference;

/**
 * WebSocket 路由。
 *
 * @author ruoyi
 */
public class NettyWebSocketRoute {

    @Getter
    private final String path;

    private final AtomicReference<NettyWebSocketConnectHandler> connectHandler = new AtomicReference<>();

    private final AtomicReference<NettyWebSocketMessageHandler> messageHandler = new AtomicReference<>();

    private final AtomicReference<NettyWebSocketDisconnectHandler> disconnectHandler = new AtomicReference<>();

    public NettyWebSocketRoute(String path) {
        this.path = path;
    }

    public NettyWebSocketRoute onConnect(NettyWebSocketConnectHandler handler) {
        connectHandler.set(handler);
        return this;
    }

    public NettyWebSocketRoute onMessage(NettyWebSocketMessageHandler handler) {
        messageHandler.set(handler);
        return this;
    }

    public NettyWebSocketRoute onDisconnect(NettyWebSocketDisconnectHandler handler) {
        disconnectHandler.set(handler);
        return this;
    }

    public void fireConnect(NettyWebSocketSession session) {
        NettyWebSocketConnectHandler handler = connectHandler.get();
        if (handler != null) {
            handler.onConnect(session);
        }
    }

    public void fireMessage(NettyWebSocketSession session, String message) {
        NettyWebSocketMessageHandler handler = messageHandler.get();
        if (handler != null) {
            handler.onMessage(session, message);
        }
    }

    public void fireDisconnect(NettyWebSocketSession session) {
        NettyWebSocketDisconnectHandler handler = disconnectHandler.get();
        if (handler != null) {
            handler.onDisconnect(session);
        }
    }
}
