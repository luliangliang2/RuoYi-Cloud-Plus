package org.dromara.common.websocket.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.common.websocket.dto.WebSocketDto;
import org.dromara.common.websocket.dto.WebSocketMessageDto;
import org.dromara.common.websocket.holder.WebSocketSessionHolder;
import org.dromara.system.api.model.LoginUser;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.dromara.common.websocket.constant.WebSocketConstants.LOGIN_USER_KEY;
import static org.dromara.common.websocket.constant.WebSocketConstants.WEB_SOCKET_TOPIC;

/**
 * 工具类
 *
 * @author zendwang
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketUtils {

    /**
     * 发送消息
     *
     * @param sessionKey session主键 一般为用户id
     * @param message    消息文本
     */
    public static void sendMessage(Long sessionKey, WebSocketMessageDto message) {
        WebSocketSession session = WebSocketSessionHolder.getSessions(sessionKey);
        sendMessage(session, message);
    }

    /**
     * 发送群组消息
     *
     * @param groupKey session主键 一般为群组id
     * @param message  消息文本
     */
    public static void sendGroupMessage(Long groupKey, WebSocketMessageDto message) {
        sendGroupMessage(String.valueOf(groupKey), message, null);
    }

    /**
     * 发送群组消息
     *
     * @param groupKey session主键 一般为群组id
     * @param message  消息文本
     */
    public static void sendGroupMessage(Long groupKey, WebSocketMessageDto message, Long excludeSessionKey) {
        sendGroupMessage(String.valueOf(groupKey), message, excludeSessionKey);
    }

    /**
     * 发送群组消息
     *
     * @param groupKey
     * @param message
     */
    public static void sendGroupMessage(String groupKey, WebSocketMessageDto message) {
        sendGroupMessage(groupKey, message, null);
    }

    /**
     * 发送群组消息
     *
     * @param groupKey
     * @param message
     * @param excludeSessionKey
     */
    public static void sendGroupMessage(String groupKey, WebSocketMessageDto message, Long excludeSessionKey) {
        Set<Long> keySet = WebSocketSessionHolder.getGroup(groupKey);
        if (ObjectUtil.isNotEmpty(keySet)) {
            for (Iterator<Long> iterator = keySet.iterator(); iterator.hasNext(); ) {
                Long sessionKey = iterator.next();
                if (ObjectUtil.isNotEmpty(excludeSessionKey)) {
                    if (!sessionKey.equals(excludeSessionKey)) {
                        sendMessage(sessionKey, message);
                    }
                } else {
                    sendMessage(sessionKey, message);
                }
            }
        }
    }

    /**
     * 订阅消息
     *
     * @param consumer 自定义处理
     */
    public static void subscribeMessage(Consumer<WebSocketDto> consumer) {
        RedisUtils.subscribe(WEB_SOCKET_TOPIC, WebSocketDto.class, consumer);
    }

    /**
     * 发布订阅的消息
     *
     * @param webSocketMessage 消息对象
     */
    public static void publishMessage(WebSocketDto webSocketMessage) {
        List<Long> unsentSessionKeys = new ArrayList<>();
        // 当前服务内session,直接发送消息
        for (Long sessionKey : webSocketMessage.getSessionKeys()) {
            if (WebSocketSessionHolder.existSession(sessionKey)) {
                WebSocketUtils.sendMessage(sessionKey, webSocketMessage.getMessage());
                continue;
            }
            unsentSessionKeys.add(sessionKey);
        }
        // 不在当前服务内session,发布订阅消息
        if (CollUtil.isNotEmpty(unsentSessionKeys)) {
            WebSocketDto broadcastMessage = new WebSocketDto();
            broadcastMessage.setMessage(webSocketMessage.getMessage());
            broadcastMessage.setSessionKeys(unsentSessionKeys);
            RedisUtils.publish(WEB_SOCKET_TOPIC, broadcastMessage, consumer -> {
                log.info(" WebSocket发送主题订阅消息topic:{} session keys:{} message:{}",
                    WEB_SOCKET_TOPIC, unsentSessionKeys, webSocketMessage.getMessage());
            });
        }
    }

    public static void sendPongMessage(WebSocketSession session) {
        sendMessage(session, new PongMessage());
    }

    public static void sendMessage(WebSocketSession session, WebSocketMessageDto message) {
        sendMessage(session, new TextMessage( JsonUtils.toJsonString(message)));
    }

    private static void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (session == null || !session.isOpen()) {
            log.error("[send] session会话已经关闭");
        } else {
            try {
                // 获取当前会话中的用户
                LoginUser loginUser = (LoginUser) session.getAttributes().get(LOGIN_USER_KEY);
                session.sendMessage(message);
                log.info("[send] sessionId: {},userId:{},userType:{},message:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType(), message);
            } catch (IOException e) {
                log.error("[send] session({}) 发送消息({}) 异常", session, message, e);
            }
        }
    }

    public static String genGroupKey(String url, Map<String, Object> attributes) {
        Long tenantId = (Long) attributes.get( TenantHelper.DYNAMIC_TENANT_KEY);
        String groupNo = getParameter(url, "groupKey");
        return genGroupKey(tenantId, groupNo);
    }

    public static String genGroupKey(Long tenantId, String groupNo) {
        String groupKey = String.valueOf(tenantId);
        if (ObjectUtil.isNotEmpty(groupNo)) {
            groupKey += (":" + groupNo);
        }
        return groupKey;
    }

    //获取url中的参数值
    private static String getParameter(String url, String key) {
        String result = "";
        Pattern pattern = Pattern.compile(key + "=([^&]*)");
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }
}
