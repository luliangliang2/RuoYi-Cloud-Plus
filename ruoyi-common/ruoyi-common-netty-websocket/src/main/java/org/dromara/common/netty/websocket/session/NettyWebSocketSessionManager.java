package org.dromara.common.netty.websocket.session;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Netty WebSocket 会话管理器。
 *
 * @author ruoyi
 */
@Slf4j
public class NettyWebSocketSessionManager {

    private final Map<String, NettyWebSocketSession> sessionMap = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> pathIndex = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> tenantIndex = new ConcurrentHashMap<>();

    private final Map<Long, Set<String>> userIndex = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> aliasIndex = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> bizIndex = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> pathTenantIndex = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> pathUserIndex = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> pathAliasIndex = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> pathBizIndex = new ConcurrentHashMap<>();

    public void add(NettyWebSocketSession session) {
        remove(session.getSessionId());
        sessionMap.put(session.getSessionId(), session);
        addIndex(pathIndex, session.getPath(), session.getSessionId());
        addIndex(tenantIndex, session.getTenantId(), session.getSessionId());
        addIndex(userIndex, session.getUserId(), session.getSessionId());
        addIndex(aliasIndex, session.getAlias(), session.getSessionId());
        addIndex(bizIndex, bizKey(session.getBizType(), session.getBizId()), session.getSessionId());
        addIndex(pathTenantIndex, pathKey(session.getPath(), session.getTenantId()), session.getSessionId());
        addIndex(pathUserIndex, pathKey(session.getPath(), session.getUserId()), session.getSessionId());
        addIndex(pathAliasIndex, pathKey(session.getPath(), session.getAlias()), session.getSessionId());
        addIndex(pathBizIndex, pathKey(session.getPath(), bizKey(session.getBizType(), session.getBizId())),
            session.getSessionId());
    }

    public void remove(String sessionId) {
        NettyWebSocketSession session = sessionMap.remove(sessionId);
        if (session == null) {
            return;
        }
        removeIndex(pathIndex, session.getPath(), sessionId);
        removeIndex(tenantIndex, session.getTenantId(), sessionId);
        removeIndex(userIndex, session.getUserId(), sessionId);
        removeIndex(aliasIndex, session.getAlias(), sessionId);
        removeIndex(bizIndex, bizKey(session.getBizType(), session.getBizId()), sessionId);
        removeIndex(pathTenantIndex, pathKey(session.getPath(), session.getTenantId()), sessionId);
        removeIndex(pathUserIndex, pathKey(session.getPath(), session.getUserId()), sessionId);
        removeIndex(pathAliasIndex, pathKey(session.getPath(), session.getAlias()), sessionId);
        removeIndex(pathBizIndex, pathKey(session.getPath(), bizKey(session.getBizType(), session.getBizId())),
            sessionId);
    }

    public NettyWebSocketSession get(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public int sendBySessionId(String sessionId, String message) {
        return sendSessions(Collections.singleton(sessionId), message);
    }

    public int sendBySessionIds(Collection<String> sessionIds, String message) {
        return sendSessions(sessionIds, message);
    }

    public int sendByPath(String path, String message) {
        return sendSessions(pathIndex.get(normalizePath(path)), message);
    }

    public int sendByTenant(String tenantId, String message) {
        return sendSessions(tenantIndex.get(tenantId), message);
    }

    public int sendByUser(Long userId, String message) {
        return sendSessions(userIndex.get(userId), message);
    }

    public int sendByAlias(String alias, String message) {
        return sendSessions(aliasIndex.get(alias), message);
    }

    public int sendByBiz(String bizType, String bizId, String message) {
        return sendSessions(bizIndex.get(bizKey(bizType, bizId)), message);
    }

    public int sendByPathAndTenant(String path, String tenantId, String message) {
        return sendSessions(pathTenantIndex.get(pathKey(normalizePath(path), tenantId)), message);
    }

    public int sendByPathAndUser(String path, Long userId, String message) {
        return sendSessions(pathUserIndex.get(pathKey(normalizePath(path), userId)), message);
    }

    public int sendByPathAndAlias(String path, String alias, String message) {
        return sendSessions(pathAliasIndex.get(pathKey(normalizePath(path), alias)), message);
    }

    public int sendByPathAndBiz(String path, String bizType, String bizId, String message) {
        return sendSessions(pathBizIndex.get(pathKey(normalizePath(path), bizKey(bizType, bizId))), message);
    }

    public int broadcast(String message) {
        return sendSessions(sessionMap.keySet(), message);
    }

    public int sendWhere(Predicate<NettyWebSocketSession> predicate, String message) {
        AtomicInteger count = new AtomicInteger();
        sessionMap.values().forEach(session -> {
            if (predicate.test(session) && send(session, message)) {
                count.incrementAndGet();
            }
        });
        return count.get();
    }

    public int count() {
        return sessionMap.size();
    }

    public Map<String, Object> stats() {
        return Map.of(
            "sessions", sessionMap.size(),
            "paths", pathIndex.size(),
            "tenants", tenantIndex.size(),
            "users", userIndex.size(),
            "aliases", aliasIndex.size(),
            "biz", bizIndex.size(),
            "pathTenants", pathTenantIndex.size(),
            "pathUsers", pathUserIndex.size(),
            "pathAliases", pathAliasIndex.size(),
            "pathBiz", pathBizIndex.size()
        );
    }

    private int sendSessions(Collection<String> sessionIds, String message) {
        if (CollUtil.isEmpty(sessionIds)) {
            return 0;
        }
        AtomicInteger count = new AtomicInteger();
        for (String sessionId : sessionIds) {
            if (send(sessionMap.get(sessionId), message)) {
                count.incrementAndGet();
            }
        }
        return count.get();
    }

    private boolean send(NettyWebSocketSession session, String message) {
        if (session == null) {
            return false;
        }
        Channel channel = session.getChannel();
        if (channel == null || !channel.isActive()) {
            remove(session.getSessionId());
            return false;
        }
        if (!channel.isWritable()) {
            log.warn("Netty WebSocket channel not writable, close session: {}", session.getSessionId());
            channel.close();
            remove(session.getSessionId());
            return false;
        }
        channel.writeAndFlush(new TextWebSocketFrame(message));
        return true;
    }

    private static <K> void addIndex(Map<K, Set<String>> index, K key, String sessionId) {
        if (key == null || (key instanceof String stringKey && StrUtil.isBlank(stringKey))) {
            return;
        }
        index.computeIfAbsent(key, ignored -> ConcurrentHashMap.newKeySet()).add(sessionId);
    }

    private static <K> void removeIndex(Map<K, Set<String>> index, K key, String sessionId) {
        if (key == null || (key instanceof String stringKey && StrUtil.isBlank(stringKey))) {
            return;
        }
        Set<String> sessions = index.get(key);
        if (sessions == null) {
            return;
        }
        sessions.remove(sessionId);
        if (sessions.isEmpty()) {
            index.remove(key);
        }
    }

    private static String bizKey(String bizType, String bizId) {
        if (StrUtil.isBlank(bizType) || StrUtil.isBlank(bizId)) {
            return null;
        }
        return bizType + ":" + bizId;
    }

    private static String pathKey(String path, Object value) {
        if (StrUtil.isBlank(path) || value == null) {
            return null;
        }
        if (value instanceof String stringValue && StrUtil.isBlank(stringValue)) {
            return null;
        }
        return path + ":" + value;
    }

    private static String normalizePath(String path) {
        if (StrUtil.isBlank(path)) {
            return path;
        }
        return path.startsWith("/") ? path : "/" + path;
    }
}
