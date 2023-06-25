package org.dromara.common.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.websocket.dto.WebSocketMessageDto;
import org.dromara.common.websocket.holder.WebSocketSessionHolder;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.dromara.system.api.model.LoginUser;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.net.URI;
import java.util.Map;

import static org.dromara.common.websocket.constant.WebSocketConstants.*;

/**
 * WebSocketHandler 实现类
 *
 * @author zendwang
 */
@Slf4j
public class PlusWebSocketHandler extends AbstractWebSocketHandler {

    /**
     * 连接成功后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get(LOGIN_USER_KEY);
        WebSocketSessionHolder.addSession(loginUser.getUserId(), session);
        //通过URL获取分组标识
        URI uri = session.getUri();
        String groupKey = WebSocketUtils.genGroupKey(uri.getQuery(),session.getAttributes());
        WebSocketSessionHolder.addGroup( groupKey, loginUser.getUserId() );
        log.info("[connect] sessionId: {},userId:{},userType:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType());
    }

    /**
     * 处理发送来的文本消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info( "handleTextMessage, 连接：" + session.getId() + "，已收到消息:" + message.getPayload() );
        Map map = JsonUtils.parseMap( message.getPayload() );
        if (PING.equals( map.get( "type" ) )) {
            WebSocketUtils.sendMessage( session, WebSocketMessageDto.builder().type( PONG ).build() );
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
    }

    /**
     * 心跳监测的回复
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        WebSocketUtils.sendPongMessage(session);
    }

    /**
     * 连接出错时
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("[transport error] sessionId: {} , exception:{}", session.getId(), exception.getMessage());
    }

    /**
     * 连接关闭后
     *
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get(LOGIN_USER_KEY);
        WebSocketSessionHolder.removeSession(loginUser.getUserId());
        //通过URL获取分组标识
        URI uri = session.getUri();
        String groupKey = WebSocketUtils.genGroupKey(uri.getQuery(), session.getAttributes());
        WebSocketSessionHolder.removeGroup( groupKey, loginUser.getUserId() );
        log.info("[disconnect] sessionId: {},userId:{},userType:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType());
    }

    /**
     * 是否支持分片消息
     *
     * @return
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
