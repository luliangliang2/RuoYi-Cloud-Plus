package com.ruoyi.common.websocket.constant;

/**
 * <p>
 * websocket的常量配置
 * </p>
 *
 * @author lyt
 * @since 2022/11/2
 */
public interface WebSocketConstants {
    /**
     * websocketSession中的参数的key
     */
    String LOGIN_USER_KEY = "loginUser";
    /**
     * 订阅的频道
     */
    String WEB_SOCKET_TOPIC = "websocket";
    /**
     * 前端心跳检查的命令
     */
    String PING = "ping";
    /**
     * 服务端心跳恢复的字符串
     */
    String PONG = "pong";
}
