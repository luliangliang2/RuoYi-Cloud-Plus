package org.ssssssss.magicapi.net.hub;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.ssssssss.magicapi.netty.WebSocketAuthInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebSocket Hub 连接上下文。
 */
public class WebSocketContext {

    private final String path;
    private final String clientId;
    private final Channel channel;
    private final WebSocketAuthInfo authInfo;
    private final Map<String, String> params;
    private final Map<String, String> headers;
    private String alias;

    public WebSocketContext(String path, String clientId, Channel channel, FullHttpRequest request, WebSocketAuthInfo authInfo) {
        this.path = path;
        this.clientId = clientId;
        this.channel = channel;
        this.authInfo = authInfo;
        this.params = parseParams(request);
        this.headers = parseHeaders(request);
    }

    public String getPath() {
        return path;
    }

    public String getClientId() {
        return clientId;
    }

    public Channel getChannel() {
        return channel;
    }

    public WebSocketAuthInfo getAuthInfo() {
        return authInfo;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTenantId() {
        return firstNonBlank(getAuthAttribute("tenantId"), getParam("tenantId"));
    }

    public String getUserId() {
        return firstNonBlank(getAuthAttribute("userId"), getParam("userId"));
    }

    public String getUsername() {
        return firstNonBlank(getAuthAttribute("username"), getParam("username"), getParam("userName"));
    }

    public String getClientid() {
        return firstNonBlank(getAuthAttribute("clientid"), getParam("clientid"), getParam("clientId"));
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public Object getAuthAttribute(String key) {
        if (authInfo == null || authInfo.getAttributes() == null) {
            return null;
        }
        return authInfo.getAttributes().get(key);
    }

    public WebSocketSession toSession() {
        WebSocketSession session = new WebSocketSession();
        session.setSessionId(channel.id().asLongText());
        session.setPath(path);
        session.setClientId(clientId);
        session.setAlias(alias);
        session.setTenantId(toString(getTenantId()));
        session.setUserId(toString(getUserId()));
        session.setUsername(toString(getUsername()));
        session.setClientid(toString(getClientid()));
        session.setParams(params);
        session.setHeaders(headers);
        session.setAuthInfo(authInfo);
        session.setChannel(channel);
        session.setConnectTime(System.currentTimeMillis());
        return session;
    }

    private static Map<String, String> parseParams(FullHttpRequest request) {
        if (request == null) {
            return Collections.emptyMap();
        }
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : decoder.parameters().entrySet()) {
            List<String> values = entry.getValue();
            if (values != null && !values.isEmpty()) {
                result.put(entry.getKey(), values.get(0));
            }
        }
        return result;
    }

    private static Map<String, String> parseHeaders(FullHttpRequest request) {
        if (request == null) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        request.headers().forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    private static String firstNonBlank(Object... values) {
        if (values == null) {
            return null;
        }
        for (Object value : values) {
            String text = toString(value);
            if (text != null && !text.trim().isEmpty()) {
                return text;
            }
        }
        return null;
    }

    private static String toString(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
