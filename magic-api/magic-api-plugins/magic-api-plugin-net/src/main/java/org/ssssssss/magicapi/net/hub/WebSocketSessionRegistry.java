package org.ssssssss.magicapi.net.hub;

import io.netty.channel.Channel;
import org.ssssssss.magicapi.netty.NettyService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Hub 会话索引。
 */
public class WebSocketSessionRegistry {

    private final NettyService nettyService;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> channelIndex = new ConcurrentHashMap<>();

    public WebSocketSessionRegistry(NettyService nettyService) {
        this.nettyService = nettyService;
    }

    public void register(WebSocketSession session) {
        if (session == null || session.getSessionId() == null) {
            return;
        }
        unregister(session.getSessionId());
        sessions.put(session.getSessionId(), session);
        if (session.getChannel() != null) {
            channelIndex.put(session.getChannel().id().asLongText(), session);
        }
    }

    public WebSocketSession unregister(String sessionId) {
        WebSocketSession session = sessions.remove(sessionId);
        if (session == null) {
            return null;
        }
        if (session.getChannel() != null) {
            channelIndex.remove(session.getChannel().id().asLongText());
        }
        return session;
    }

    public WebSocketSession unregister(Channel channel) {
        if (channel == null) {
            return null;
        }
        WebSocketSession session = channelIndex.get(channel.id().asLongText());
        return session == null ? null : unregister(session.getSessionId());
    }

    public WebSocketSession getByChannel(Channel channel) {
        return channel == null ? null : channelIndex.get(channel.id().asLongText());
    }

    public Collection<WebSocketSession> getSessions() {
        return Collections.unmodifiableCollection(sessions.values());
    }

    public Collection<WebSocketSession> findByPath(String tenantId, String path) {
        Map<String, Object> rules = new java.util.HashMap<>();
        rules.put("path", path);
        if (!isBlank(tenantId)) {
            rules.put("tenantId", tenantId);
        }
        return find(WebSocketPushRule.from(rules), path);
    }

    public Collection<WebSocketSession> find(WebSocketPushRule rule, String defaultPath) {
        Collection<WebSocketSession> result = new ArrayList<>();
        for (WebSocketSession session : sessions.values()) {
            if (matches(rule, session, defaultPath)) {
                result.add(session);
            }
        }
        return result;
    }

    public int push(Collection<WebSocketSession> targets, Object message) {
        if (targets == null) {
            return 0;
        }
        int count = 0;
        for (WebSocketSession session : targets) {
            if (session != null && session.isActive()) {
                nettyService.sendWebSocketMessage(session.getChannel(), message);
                count++;
            }
        }
        return count;
    }

    private boolean matches(WebSocketPushRule rule, WebSocketSession session, String defaultPath) {
        if (session == null || !session.isActive()) {
            return false;
        }
        String path = firstNonBlank(rule == null ? null : rule.getString("path"), defaultPath);
        if (!isBlank(path) && !equals(path, session.getPath())) {
            return false;
        }
        if (rule == null || rule.isEmpty()) {
            return true;
        }
        for (Map.Entry<String, Object> entry : rule.getRules().entrySet()) {
            String key = entry.getKey();
            if ("path".equals(key)) {
                continue;
            }
            String expected = entry.getValue() == null ? null : String.valueOf(entry.getValue());
            if (!equals(expected, getValue(session, key))) {
                return false;
            }
        }
        return true;
    }

    private static String getValue(WebSocketSession session, String key) {
        if ("sessionId".equals(key)) {
            return session.getSessionId();
        }
        if ("clientId".equals(key)) {
            return session.getClientId();
        }
        if ("alias".equals(key)) {
            return session.getAlias();
        }
        if ("tenantId".equals(key)) {
            return session.getTenantId();
        }
        if ("userId".equals(key)) {
            return session.getUserId();
        }
        if ("username".equals(key) || "userName".equals(key)) {
            return session.getUsername();
        }
        if ("clientid".equals(key) || "clientIdHeader".equals(key)) {
            return session.getClientid();
        }
        if ("path".equals(key)) {
            return session.getPath();
        }
        if (session.getParams() != null && session.getParams().containsKey(key)) {
            return session.getParams().get(key);
        }
        return null;
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static boolean equals(String left, String right) {
        return left == null ? right == null : left.equals(right);
    }
}
