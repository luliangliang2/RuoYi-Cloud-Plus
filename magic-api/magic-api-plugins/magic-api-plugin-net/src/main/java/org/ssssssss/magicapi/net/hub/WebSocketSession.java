package org.ssssssss.magicapi.net.hub;

import io.netty.channel.Channel;
import org.ssssssss.magicapi.netty.WebSocketAuthInfo;

import java.util.Collections;
import java.util.Map;

/**
 * WebSocket Hub 在线会话。
 */
public class WebSocketSession {

    private String sessionId;
    private String path;
    private String clientId;
    private String alias;
    private String tenantId;
    private String userId;
    private String username;
    private String clientid;
    private Map<String, String> params = Collections.emptyMap();
    private Map<String, String> headers = Collections.emptyMap();
    private WebSocketAuthInfo authInfo;
    private Channel channel;
    private long connectTime;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getClientid() { return clientid; }
    public void setClientid(String clientid) { this.clientid = clientid; }
    public Map<String, String> getParams() { return params; }
    public void setParams(Map<String, String> params) { this.params = params == null ? Collections.emptyMap() : params; }
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers == null ? Collections.emptyMap() : headers; }
    public WebSocketAuthInfo getAuthInfo() { return authInfo; }
    public void setAuthInfo(WebSocketAuthInfo authInfo) { this.authInfo = authInfo; }
    public Channel getChannel() { return channel; }
    public void setChannel(Channel channel) { this.channel = channel; }
    public long getConnectTime() { return connectTime; }
    public void setConnectTime(long connectTime) { this.connectTime = connectTime; }

    public boolean isActive() {
        return channel != null && channel.isActive();
    }
}
