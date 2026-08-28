package org.ssssssss.magicapi.net.hub;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import org.ssssssss.magicapi.net.auth.RuoYiWebSocketAuthProvider;
import org.ssssssss.magicapi.netty.NettyService;
import org.ssssssss.magicapi.netty.WebSocketAuthInfo;
import org.ssssssss.magicapi.netty.WebSocketAuthProvider;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个端口多个路径的 WebSocket Hub。
 */
public class WebSocketHub {

    private final String name;
    private final int port;
    private final boolean ruoyiAuth;
    private final NettyService nettyService;
    private final WebSocketAuthProvider authProvider;
    private final Map<String, WebSocketRoute> routes = new ConcurrentHashMap<>();
    private final WebSocketSessionRegistry sessionRegistry;
    private Channel serverChannel;

    public WebSocketHub(String name, int port, boolean ruoyiAuth, NettyService nettyService) {
        this.name = name;
        this.port = port;
        this.ruoyiAuth = ruoyiAuth;
        this.nettyService = nettyService;
        this.authProvider = ruoyiAuth ? new RuoYiWebSocketAuthProvider() : null;
        this.sessionRegistry = new WebSocketSessionRegistry(nettyService);
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public WebSocketHub start() {
        if (serverChannel == null || !serverChannel.isActive()) {
            serverChannel = nettyService.startWebSocketHub(
                port,
                false,
                null,
                null,
                ruoyiAuth,
                authProvider,
                this::findRoute,
                this::register,
                this::onMessage,
                this::onConnect,
                this::onDisconnect
            );
        }
        return this;
    }

    public WebSocketRoute route(String path) {
        String normalizedPath = WebSocketRoute.normalizePath(path);
        return routes.computeIfAbsent(normalizedPath, WebSocketRoute::new);
    }

    public boolean hasRoute(String path) {
        return routes.containsKey(WebSocketRoute.normalizePath(path));
    }

    public WebSocketRoute findRoute(String path) {
        return routes.get(WebSocketRoute.normalizePath(path));
    }

    public int push(String path, Map<String, Object> params, Object message) {
        WebSocketPushRule rule = WebSocketPushRule.from(params);
        Collection<WebSocketSession> targets = sessionRegistry.find(rule, WebSocketRoute.normalizePath(path));
        return sessionRegistry.push(targets, message);
    }

    public int push(Map<String, Object> rule, Object message) {
        Collection<WebSocketSession> targets = sessionRegistry.find(WebSocketPushRule.from(rule), null);
        return sessionRegistry.push(targets, message);
    }

    public int pushByParam(String path, String key, Object value, Object message) {
        Map<String, Object> rule = new java.util.HashMap<>();
        rule.put(key, value);
        return push(path, rule, message);
    }

    public int pushByParam(String path, String tenantId, String key, Object value, Object message) {
        Map<String, Object> rule = new java.util.HashMap<>();
        rule.put("tenantId", tenantId);
        rule.put(key, value);
        return push(path, rule, message);
    }

    public int pushByAlias(String path, String alias, Object message) {
        Map<String, Object> rule = new java.util.HashMap<>();
        rule.put("alias", alias);
        return push(path, rule, message);
    }

    public int broadcast(String path, Object message) {
        return sessionRegistry.push(sessionRegistry.findByPath(null, WebSocketRoute.normalizePath(path)), message);
    }

    public int broadcast(String path, String tenantId, Object message) {
        return sessionRegistry.push(sessionRegistry.findByPath(tenantId, WebSocketRoute.normalizePath(path)), message);
    }

    public Collection<WebSocketSession> sessions() {
        return sessionRegistry.getSessions();
    }

    public void close() {
        if (serverChannel != null) {
            nettyService.closeChannel(serverChannel);
        }
    }

    private String register(WebSocketContext context) {
        WebSocketRoute route = findRoute(context.getPath());
        if (route == null) {
            return null;
        }
        String alias = route.register(context);
        context.setAlias(alias);
        WebSocketSession session = context.toSession();
        sessionRegistry.register(session);
        return alias;
    }

    private void onConnect(WebSocketContext context) {
        WebSocketSession session = sessionRegistry.getByChannel(context.getChannel());
        if (session != null) {
            context.setAlias(session.getAlias());
        }
        WebSocketRoute route = findRoute(context.getPath());
        if (route != null) {
            route.fireConnect(context);
        }
    }

    private void onMessage(WebSocketContext context, Object message) {
        WebSocketSession session = sessionRegistry.getByChannel(context.getChannel());
        if (session != null) {
            context.setAlias(session.getAlias());
        }
        WebSocketRoute route = findRoute(context.getPath());
        if (route != null) {
            route.fireMessage(context, message);
        }
    }

    private void onDisconnect(WebSocketContext context) {
        WebSocketSession session = sessionRegistry.unregister(context.getChannel());
        if (session != null) {
            context.setAlias(session.getAlias());
        }
        WebSocketRoute route = findRoute(context.getPath());
        if (route != null) {
            route.fireDisconnect(context);
        }
    }

    public WebSocketAuthInfo authenticate(FullHttpRequest request) {
        if (!ruoyiAuth) {
            return WebSocketAuthInfo.ANONYMOUS;
        }
        return authProvider == null ? null : authProvider.authenticate(request);
    }
}
