package com.ruoyi.common.websocket.holder;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 用于保存当前所有在线的会话信息
 *
 * @author lyt
 * @since 2022/11/1
 */
public final class WebSocketSessionHolder {
    private static final Map<Long, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();

    public static void addSession(Long sessionKey, WebSocketSession session) {
        USER_SESSION_MAP.put( sessionKey, session );
    }


    public static void removeSession(Long sessionKey) {
        if (USER_SESSION_MAP.containsKey( sessionKey )) {
            USER_SESSION_MAP.remove( sessionKey );
        }
    }

    public static WebSocketSession getSessions(Long shopKey) {
        return USER_SESSION_MAP.get( shopKey );
    }

}
