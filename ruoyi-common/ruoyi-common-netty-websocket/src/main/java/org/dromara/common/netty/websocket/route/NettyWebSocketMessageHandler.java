package org.dromara.common.netty.websocket.route;

import org.dromara.common.netty.websocket.session.NettyWebSocketSession;

@FunctionalInterface
public interface NettyWebSocketMessageHandler {

    void onMessage(NettyWebSocketSession session, String message);
}
