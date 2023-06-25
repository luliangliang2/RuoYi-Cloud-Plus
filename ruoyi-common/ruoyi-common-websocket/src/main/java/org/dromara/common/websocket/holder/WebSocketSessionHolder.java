package org.dromara.common.websocket.holder;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 用于保存当前所有在线的会话信息
 *
 * @author zendwang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketSessionHolder {

    private static final Map<Long, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Set<Long>> GROUP_SESSION_MAP = new ConcurrentHashMap<>();

    public static void addSession(Long sessionKey, WebSocketSession session) {
        USER_SESSION_MAP.put(sessionKey, session);
    }

    public static void removeSession(Long sessionKey) {
        if (USER_SESSION_MAP.containsKey(sessionKey)) {
            USER_SESSION_MAP.remove(sessionKey);
        }
    }

    public static WebSocketSession getSessions(Long sessionKey) {
        return USER_SESSION_MAP.get(sessionKey);
    }

    public static Set<Long> getGroup(String groupKey) {
        return GROUP_SESSION_MAP.get( groupKey );
    }

    public static Boolean existSession(Long sessionKey) {
        return USER_SESSION_MAP.containsKey(sessionKey);
    }

    public static void addGroup(String groupKey, Long sessionKey) {
        Set<Long> keySet;
        if (GROUP_SESSION_MAP.containsKey( groupKey )) {
            keySet = GROUP_SESSION_MAP.get( groupKey );
        } else {
            keySet = new ConcurrentHashSet<>();
        }
        keySet.add( sessionKey );
        GROUP_SESSION_MAP.put( groupKey, keySet );
    }

    public static void removeGroup(String groupKey, Long sessionKey) {
        if (GROUP_SESSION_MAP.containsKey( groupKey )) {
            Set<Long> keySet = GROUP_SESSION_MAP.get( groupKey );
            if (keySet.contains( sessionKey )) {
                keySet.remove( sessionKey );
            }
        }
    }
}
