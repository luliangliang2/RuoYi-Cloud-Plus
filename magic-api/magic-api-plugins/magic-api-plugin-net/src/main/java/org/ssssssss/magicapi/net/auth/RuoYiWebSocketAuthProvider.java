package org.ssssssss.magicapi.net.auth;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssssssss.magicapi.netty.WebSocketAuthInfo;
import org.ssssssss.magicapi.netty.WebSocketAuthProvider;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 若依 Sa-Token WebSocket 握手认证。
 *
 * <p>通过反射调用 Sa-Token/RuoYi 类，避免 Net 插件强依赖若依工程。</p>
 */
public class RuoYiWebSocketAuthProvider implements WebSocketAuthProvider {

    private static final Logger logger = LoggerFactory.getLogger(RuoYiWebSocketAuthProvider.class);

    private static final Pattern JWT_PATTERN = Pattern.compile("eyJ[^,\\s]*\\.[^,\\s]*\\.[^,\\s]*");

    @Override
    public WebSocketAuthInfo authenticate(FullHttpRequest request) {
        String token = normalizeToken(firstNonBlank(
            request.headers().get("Authorization"),
            request.headers().get("authorization"),
            request.headers().get("token"),
            request.headers().get("Sec-WebSocket-Protocol"),
            getQueryParam(request, "Authorization"),
            getQueryParam(request, "authorization"),
            getQueryParam(request, "token")
        ));
        String requestClientId = firstNonBlank(
            request.headers().get("clientid"),
            request.headers().get("ClientID"),
            request.headers().get("clientId"),
            getQueryParam(request, "clientid"),
            getQueryParam(request, "ClientID"),
            getQueryParam(request, "clientId")
        );
        if (isBlank(token)) {
            logger.warn("WebSocket 若依鉴权失败：未读取到 token");
            return null;
        }
        try {
            Class<?> stpUtilClass = Class.forName("cn.dev33.satoken.stp.StpUtil");
            Method getLoginIdByToken = stpUtilClass.getMethod("getLoginIdByToken", String.class);
            Object loginId = getLoginIdByToken.invoke(null, token);
            if (loginId == null) {
                logger.warn("WebSocket 若依鉴权失败：token 无效或已过期");
                return null;
            }

            Object tokenClientId = invokeStatic(stpUtilClass, "getExtra", new Class<?>[]{String.class, String.class}, token, "clientid");
            if (!isBlank(requestClientId) && tokenClientId != null && !requestClientId.equals(String.valueOf(tokenClientId))) {
                logger.warn("WebSocket 若依鉴权失败：clientid 与 token 不匹配");
                return null;
            }

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loginId", loginId);
            attributes.put("token", token);
            attributes.put("clientid", tokenClientId);
            String userId = toString(invokeStatic(stpUtilClass, "getExtra", new Class<?>[]{String.class, String.class}, token, "userId"));
            String username = toString(invokeStatic(stpUtilClass, "getExtra", new Class<?>[]{String.class, String.class}, token, "userName"));
            String tenantId = toString(invokeStatic(stpUtilClass, "getExtra", new Class<?>[]{String.class, String.class}, token, "tenantId"));
            attributes.put("userId", userId);
            attributes.put("username", username);
            attributes.put("tenantId", tenantId);
            return new WebSocketAuthInfo(true, userId, username, tenantId, attributes);
        } catch (ClassNotFoundException e) {
            logger.warn("WebSocket 若依鉴权失败：当前运行环境未发现 Sa-Token");
            return null;
        } catch (Exception e) {
            logger.warn("WebSocket 若依鉴权失败：{}", e.getMessage());
            return null;
        }
    }

    private static Object invokeStatic(Class<?> clazz, String method, Class<?>[] parameterTypes, Object... args) throws Exception {
        Method target = clazz.getMethod(method, parameterTypes);
        return target.invoke(null, args);
    }

    private static String getQueryParam(FullHttpRequest request, String key) {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        List<String> values = decoder.parameters().get(key);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private static String normalizeToken(String token) {
        if (isBlank(token)) {
            return token;
        }
        token = URLDecoder.decode(token.trim(), StandardCharsets.UTF_8);
        if (token.regionMatches(true, 0, "Bearer ", 0, 7)) {
            token = token.substring(7);
        }
        if (token.regionMatches(true, 0, "Bearer,", 0, 7)) {
            token = token.substring(7).trim();
        }
        Matcher matcher = JWT_PATTERN.matcher(token);
        return matcher.find() ? matcher.group() : token;
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

    private static String toString(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
