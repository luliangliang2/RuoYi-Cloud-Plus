package org.dromara.common.netty.websocket.route;

import org.dromara.common.netty.websocket.session.NettyWebSocketSession;

@FunctionalInterface
public interface NettyWebSocketDisconnectHandler {

    void onDisconnect(NettyWebSocketSession session);
}
