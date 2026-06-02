package org.dromara.magicapi.websocket;

import org.dromara.common.netty.websocket.core.NettyWebSocketMessagePublisher;
import org.dromara.common.netty.websocket.route.NettyWebSocketRoute;
import org.dromara.common.netty.websocket.route.NettyWebSocketRouteRegistry;
import org.dromara.common.netty.websocket.session.NettyWebSocketSessionManager;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.magicapi.modules.DynamicModule;
import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.annotation.Comment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Magic API Netty WebSocket 模块。
 *
 * <p>脚本中使用：import ws;</p>
 *
 * @author ruoyi
 */
@Component
@MagicModule("ws")
public class MagicWebSocketModule implements DynamicModule<MagicWebSocketModule> {

    private final NettyWebSocketRouteRegistry routeRegistry;

    private final NettyWebSocketMessagePublisher publisher;

    private final NettyWebSocketSessionManager sessionManager;

    public MagicWebSocketModule(
        NettyWebSocketRouteRegistry routeRegistry,
        NettyWebSocketMessagePublisher publisher,
        NettyWebSocketSessionManager sessionManager) {
        this.routeRegistry = routeRegistry;
        this.publisher = publisher;
        this.sessionManager = sessionManager;
    }

    
    public MagicWebSocketModule getDynamicModule(MagicScriptContext context) {
        return this;
    }

    @Comment("注册或获取 WebSocket 路由")
    public NettyWebSocketRoute route(@Comment(name = "path", value = "WebSocket路径") String path) {
        return routeRegistry.route(path);
    }

    @Comment("按会话ID推送")
    public int pushBySession(
        @Comment(name = "sessionId", value = "会话ID") String sessionId,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendBySessionId(sessionId, message);
    }

    @Comment("按路径推送")
    public int pushByPath(
        @Comment(name = "path", value = "路径") String path,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByPath(path, message);
    }

    @Comment("按租户推送")
    public int pushByTenant(
        @Comment(name = "tenantId", value = "租户ID") String tenantId,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByTenant(tenantId, message);
    }

    @Comment("按用户推送")
    public int pushByUser(
        @Comment(name = "userId", value = "用户ID") Long userId,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByUser(userId, message);
    }

    @Comment("按别名推送")
    public int pushByAlias(
        @Comment(name = "alias", value = "连接别名") String alias,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByAlias(alias, message);
    }

    @Comment("按业务标识推送")
    public int pushByBiz(
        @Comment(name = "bizType", value = "业务类型") String bizType,
        @Comment(name = "bizId", value = "业务ID") String bizId,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByBiz(bizType, bizId, message);
    }

    @Comment("按路径和租户推送")
    public int pushByPathAndTenant(
        @Comment(name = "path", value = "路径") String path,
        @Comment(name = "tenantId", value = "租户ID") String tenantId,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByPathAndTenant(path, tenantId, message);
    }

    @Comment("按路径和用户推送")
    public int pushByPathAndUser(
        @Comment(name = "path", value = "路径") String path,
        @Comment(name = "userId", value = "用户ID") Long userId,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByPathAndUser(path, userId, message);
    }

    @Comment("按路径和别名推送")
    public int pushByPathAndAlias(
        @Comment(name = "path", value = "路径") String path,
        @Comment(name = "alias", value = "连接别名") String alias,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByPathAndAlias(path, alias, message);
    }

    @Comment("按路径和业务标识推送")
    public int pushByPathAndBiz(
        @Comment(name = "path", value = "路径") String path,
        @Comment(name = "bizType", value = "业务类型") String bizType,
        @Comment(name = "bizId", value = "业务ID") String bizId,
        @Comment(name = "message", value = "消息内容") String message) {
        return publisher.sendByPathAndBiz(path, bizType, bizId, message);
    }

    @Comment("广播推送")
    public int broadcast(@Comment(name = "message", value = "消息内容") String message) {
        return publisher.broadcast(message);
    }

    @Comment("查看 WebSocket 统计")
    public Map<String, Object> stats() {
        return sessionManager.stats();
    }
}
