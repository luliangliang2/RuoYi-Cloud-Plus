package com.ruoyi.common.websocket.utils;

import com.ruoyi.common.core.utils.JsonUtils;
import com.ruoyi.common.redis.utils.RedisUtils;
import com.ruoyi.common.websocket.dto.WebSocketMessageDto;
import com.ruoyi.common.websocket.holder.WebSocketSessionHolder;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.function.Consumer;

import static com.ruoyi.common.websocket.constant.WebSocketConstants.LOGIN_USER_KEY;
import static com.ruoyi.common.websocket.constant.WebSocketConstants.WEB_SOCKET_TOPIC;

/**
 * 工具类
 *
 * @author lyt
 * @since 2022/11/1
 */
@Slf4j
public class WebSocketUtil {
    public static void sendMessage(Long userId, String message) {
        WebSocketSession session = WebSocketSessionHolder.getSessions( userId );
        sendMessage( session, message );
    }

    public static void sendMessage(WebSocketMessageDto webSocketMessage) {
        String message = JsonUtils.toJsonString( webSocketMessage.getMessage() );
        WebSocketSession session = WebSocketSessionHolder.getSessions( webSocketMessage.getUserId() );
        sendMessage( session, message );
    }

    public static void subscribeMessage(Consumer<WebSocketMessageDto> consumer) {
        RedisUtils.subscribe( WEB_SOCKET_TOPIC, WebSocketMessageDto.class, consumer );
    }

    public static void publishMessage(WebSocketMessageDto webSocketMessage) {
        RedisUtils.publish( WEB_SOCKET_TOPIC, webSocketMessage );
    }

    public static void sendPongMessage(WebSocketSession session) {
        sendMessage( session, new PongMessage() );
    }

    public static void sendMessage(WebSocketSession session, String message) {
        sendMessage( session, new TextMessage( message ) );
    }

    private static void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (session == null || !session.isOpen()) {
            log.error( "[send] session会话已经关闭" );
        } else {
            try {
                LoginUser loginUser = (LoginUser) session.getAttributes().get( LOGIN_USER_KEY );
                session.sendMessage( message );
                log.info( "[send] sessionId: {},userId:{},userType:{},message:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType(), message );
            } catch (IOException e) {
                log.error( "[send] session({}) 发送消息({}) 异常", session, message, e );
            }
        }
    }
}
