package org.ssssssss.magicapi.netty;

import io.netty.util.AttributeKey;

/**
 * WebSocket Channel 属性。
 */
public final class WebSocketAttributes {

    public static final AttributeKey<String> CLIENT_ID = AttributeKey.valueOf("magic.websocket.clientId");

    public static final AttributeKey<String> CLIENT_ALIAS = AttributeKey.valueOf("magic.websocket.clientAlias");

    public static final AttributeKey<WebSocketAuthInfo> AUTH_INFO = AttributeKey.valueOf("magic.websocket.authInfo");

    public static final AttributeKey<String> SERVER_CHANNEL_ID = AttributeKey.valueOf("magic.websocket.serverChannelId");

    private WebSocketAttributes() {
    }
}
