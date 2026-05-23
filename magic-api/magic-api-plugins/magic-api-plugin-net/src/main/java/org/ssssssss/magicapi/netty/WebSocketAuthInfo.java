package org.ssssssss.magicapi.netty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 握手认证结果。
 */
public class WebSocketAuthInfo {

    public static final WebSocketAuthInfo ANONYMOUS = new WebSocketAuthInfo(false, null, null, null, Collections.emptyMap());

    private final boolean authenticated;

    private final String userId;

    private final String username;

    private final String tenantId;

    private final Map<String, Object> attributes;

    public WebSocketAuthInfo(boolean authenticated, String userId, String username, String tenantId, Map<String, Object> attributes) {
        this.authenticated = authenticated;
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
        this.attributes = attributes == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(attributes));
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
