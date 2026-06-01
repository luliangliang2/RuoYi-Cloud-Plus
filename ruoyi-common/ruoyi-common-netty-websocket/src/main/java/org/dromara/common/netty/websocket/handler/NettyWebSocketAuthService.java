package org.dromara.common.netty.websocket.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import org.dromara.common.netty.websocket.config.properties.NettyWebSocketProperties;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.api.model.LoginUser;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Netty WebSocket 鉴权服务。
 *
 * @author ruoyi
 */
public class NettyWebSocketAuthService {

    private static final Pattern JWT_PATTERN = Pattern.compile("eyJ[^,\\s]*\\.[^,\\s]*\\.[^,\\s]*");

    private final NettyWebSocketProperties properties;

    public NettyWebSocketAuthService(NettyWebSocketProperties properties) {
        this.properties = properties;
    }

    public LoginUser authenticate(Map<String, String> headers, Map<String, List<String>> queryParams) {
        if (!properties.isAuthEnabled()) {
            return null;
        }
        String token = extractToken(headers, queryParams);
        if (StrUtil.isBlank(token)) {
            throw new IllegalArgumentException("未能读取到有效 token");
        }
        if (StpUtil.getLoginIdByToken(token) == null) {
            throw new IllegalArgumentException("token 无效或已过期");
        }
        LoginUser loginUser = LoginHelper.getLoginUser(token);
        if (loginUser == null) {
            throw new IllegalArgumentException("token 无效");
        }
        return loginUser;
    }

    public String extractToken(Map<String, String> headers, Map<String, List<String>> queryParams) {
        String token = headers.get(properties.getTokenName().toLowerCase());
        if (StrUtil.isBlank(token)) {
            token = headers.get("authorization");
        }
        if (StrUtil.isBlank(token) && properties.isAllowQueryToken()) {
            token = firstQueryValue(queryParams, properties.getTokenName());
            if (StrUtil.isBlank(token)) {
                token = firstQueryValue(queryParams, "authorization");
            }
            if (StrUtil.isBlank(token)) {
                token = firstQueryValue(queryParams, "token");
            }
        }
        if (StrUtil.isBlank(token) && properties.isAllowProtocolToken()) {
            token = headers.get("sec-websocket-protocol");
        }
        return normalizeToken(token);
    }

    private String firstQueryValue(Map<String, List<String>> queryParams, String key) {
        if (queryParams == null || StrUtil.isBlank(key)) {
            return null;
        }
        List<String> values = queryParams.get(key);
        if (values == null) {
            values = queryParams.get(key.toLowerCase());
        }
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private String normalizeToken(String token) {
        if (StrUtil.isBlank(token)) {
            return token;
        }
        token = URLDecoder.decode(token.trim(), StandardCharsets.UTF_8);
        token = StrUtil.removePrefixIgnoreCase(token, "Bearer ");
        Matcher matcher = JWT_PATTERN.matcher(token);
        if (matcher.find()) {
            return matcher.group();
        }
        return token;
    }
}
