package org.dromara.common.netty.websocket.handler;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.netty.websocket.config.properties.NettyWebSocketProperties;
import org.dromara.common.netty.websocket.constant.NettyWebSocketConstants;
import org.dromara.common.netty.websocket.route.NettyWebSocketRoute;
import org.dromara.common.netty.websocket.route.NettyWebSocketRouteRegistry;
import org.dromara.common.netty.websocket.session.NettyWebSocketSession;
import org.dromara.common.netty.websocket.session.NettyWebSocketSessionManager;
import org.dromara.system.api.model.LoginUser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Netty WebSocket 服务端处理器。
 *
 * @author ruoyi
 */
@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private final NettyWebSocketProperties properties;

    private final NettyWebSocketAuthService authService;

    private final NettyWebSocketSessionManager sessionManager;

    private final NettyWebSocketRouteRegistry routeRegistry;

    private WebSocketServerHandshaker handshaker;

    public NettyWebSocketServerHandler(
        NettyWebSocketProperties properties,
        NettyWebSocketAuthService authService,
        NettyWebSocketSessionManager sessionManager,
        NettyWebSocketRouteRegistry routeRegistry) {
        this.properties = properties;
        this.authService = authService;
        this.sessionManager = sessionManager;
        this.routeRegistry = routeRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest request) {
            handleHandshake(ctx, request);
            return;
        }
        if (msg instanceof TextWebSocketFrame frame) {
            handleText(ctx, frame);
            return;
        }
        if (msg instanceof PingWebSocketFrame frame) {
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (msg instanceof CloseWebSocketFrame) {
            ctx.close();
        }
    }

    private void handleHandshake(ChannelHandlerContext ctx, FullHttpRequest request) {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        String path = decoder.path();
        if (!isAllowedPath(path)) {
            ctx.close();
            return;
        }
        try {
            Map<String, String> headers = new HashMap<>();
            request.headers().forEach(entry -> headers.put(entry.getKey().toLowerCase(), entry.getValue()));
            LoginUser loginUser = authService.authenticate(headers, decoder.parameters());
            String token = authService.extractToken(headers, decoder.parameters());
            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                websocketLocation(request, path), websocketProtocol(headers), true, properties.getMaxFramePayloadLength());
            handshaker = factory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                return;
            }
            handshaker.handshake(ctx.channel(), request);
            NettyWebSocketSession session = buildSession(ctx, path, decoder.parameters(), loginUser, token);
            ctx.channel().attr(io.netty.util.AttributeKey.valueOf(NettyWebSocketConstants.CHANNEL_SESSION_KEY)).set(session);
            sessionManager.add(session);
            NettyWebSocketRoute route = routeRegistry.get(path);
            if (route != null) {
                route.fireConnect(session);
            }
            log.info("Netty WebSocket connected sessionId={}, path={}, alias={}, bizType={}, bizId={}",
                session.getSessionId(), path, session.getAlias(), session.getBizType(), session.getBizId());
        } catch (Exception e) {
            log.warn("Netty WebSocket handshake failed: {}", e.getMessage());
            ctx.close();
        }
    }

    @SuppressWarnings("unchecked")
    private void handleText(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        NettyWebSocketSession session = (NettyWebSocketSession) ctx.channel()
            .attr(io.netty.util.AttributeKey.valueOf(NettyWebSocketConstants.CHANNEL_SESSION_KEY)).get();
        if (session == null) {
            ctx.close();
            return;
        }
        session.setLastActiveTime(System.currentTimeMillis());
        NettyWebSocketRoute route = routeRegistry.get(session.getPath());
        if (route != null) {
            route.fireMessage(session, frame.text());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        removeSession(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("Netty WebSocket exception: {}", cause.getMessage());
        ctx.close();
    }

    @SuppressWarnings("unchecked")
    private void removeSession(ChannelHandlerContext ctx) {
        NettyWebSocketSession session = (NettyWebSocketSession) ctx.channel()
            .attr(io.netty.util.AttributeKey.valueOf(NettyWebSocketConstants.CHANNEL_SESSION_KEY)).get();
        if (session == null) {
            return;
        }
        sessionManager.remove(session.getSessionId());
        NettyWebSocketRoute route = routeRegistry.get(session.getPath());
        if (route != null) {
            route.fireDisconnect(session);
        }
        log.info("Netty WebSocket disconnected sessionId={}", session.getSessionId());
    }

    private boolean isAllowedPath(String path) {
        List<String> pathPatterns = properties.getPathPatterns();
        if (pathPatterns == null || pathPatterns.isEmpty()) {
            return path.startsWith(NettyWebSocketConstants.DEFAULT_PATH);
        }
        return pathPatterns.stream().anyMatch(pattern -> path.equals(pattern) || path.startsWith(StrUtil.removeSuffix(pattern, "/**")));
    }

    private String websocketLocation(FullHttpRequest request, String path) {
        String host = request.headers().get("Host");
        return "ws://" + host + path;
    }

    private String websocketProtocol(Map<String, String> headers) {
        String protocol = headers.get("sec-websocket-protocol");
        if (StrUtil.isBlank(protocol)) {
            return null;
        }
        return StrUtil.trim(StrUtil.split(protocol, ',').get(0));
    }

    private NettyWebSocketSession buildSession(
        ChannelHandlerContext ctx,
        String path,
        Map<String, List<String>> params,
        LoginUser loginUser,
        String token) {
        long now = System.currentTimeMillis();
        return NettyWebSocketSession.builder()
            .sessionId(ctx.channel().id().asLongText())
            .channel(ctx.channel())
            .path(path)
            .token(token)
            .loginUser(loginUser)
            .tenantId(loginUser == null ? first(params, "tenantId") : loginUser.getTenantId())
            .userId(loginUser == null ? null : loginUser.getUserId())
            .username(loginUser == null ? null : loginUser.getUsername())
            .alias(first(params, "alias"))
            .bizType(first(params, "bizType"))
            .bizId(first(params, "bizId"))
            .params(flattenParams(params))
            .connectTime(now)
            .lastActiveTime(now)
            .build();
    }

    private Map<String, String> flattenParams(Map<String, List<String>> params) {
        if (params == null || params.isEmpty()) {
            return Map.of();
        }
        return params.entrySet().stream()
            .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().get(0),
                (left, right) -> left,
                LinkedHashMap::new));
    }

    private String first(Map<String, List<String>> params, String key) {
        List<String> values = params.get(key);
        return values == null || values.isEmpty() ? null : values.get(0);
    }
}
