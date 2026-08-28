package org.ssssssss.magicapi.netty;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * WebSocket 握手认证扩展点。
 */
@FunctionalInterface
public interface WebSocketAuthProvider {

    /**
     * 认证握手请求。
     *
     * @param request 握手请求
     * @return 认证信息，返回 null 表示认证失败
     */
    WebSocketAuthInfo authenticate(FullHttpRequest request);
}
