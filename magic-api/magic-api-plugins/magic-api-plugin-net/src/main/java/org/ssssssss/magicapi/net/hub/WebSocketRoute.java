package org.ssssssss.magicapi.net.hub;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * WebSocket Hub 路由执行器。
 */
public class WebSocketRoute {

    private final String path;
    private Consumer<WebSocketContext> connectHandler;
    private Consumer<WebSocketContext> disconnectHandler;
    private BiConsumer<WebSocketContext, Object> messageHandler;
    private BiFunction<WebSocketContext, Object, String> registerHandler;

    public WebSocketRoute(String path) {
        this.path = normalizePath(path);
    }

    public String getPath() {
        return path;
    }

    public WebSocketRoute onConnect(Consumer<WebSocketContext> handler) {
        this.connectHandler = handler;
        return this;
    }

    public WebSocketRoute onDisconnect(Consumer<WebSocketContext> handler) {
        this.disconnectHandler = handler;
        return this;
    }

    public WebSocketRoute onMessage(BiConsumer<WebSocketContext, Object> handler) {
        this.messageHandler = handler;
        return this;
    }

    public WebSocketRoute onRegister(BiFunction<WebSocketContext, Object, String> handler) {
        this.registerHandler = handler;
        return this;
    }

    void fireConnect(WebSocketContext context) {
        if (connectHandler != null) {
            connectHandler.accept(context);
        }
    }

    void fireDisconnect(WebSocketContext context) {
        if (disconnectHandler != null) {
            disconnectHandler.accept(context);
        }
    }

    void fireMessage(WebSocketContext context, Object message) {
        if (messageHandler != null) {
            messageHandler.accept(context, message);
        }
    }

    String register(WebSocketContext context) {
        if (registerHandler != null) {
            return registerHandler.apply(context, context);
        }
        String alias = context.getParam("alias");
        if (alias != null && !alias.isEmpty()) {
            return alias;
        }
        String userId = context.getUserId();
        if (userId != null && !userId.isEmpty()) {
            return userId;
        }
        return "client_" + context.getClientId().hashCode();
    }

    static String normalizePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "/websocket";
        }
        return path.startsWith("/") ? path : "/" + path;
    }
}
