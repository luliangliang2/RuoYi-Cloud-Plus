package com.ruoyi.common.websocket.handler;

import com.ruoyi.common.websocket.dto.WebSocketMessageDto;
import com.ruoyi.common.websocket.holder.WebSocketSessionHolder;
import com.ruoyi.common.websocket.utils.WebSocketUtil;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import static com.ruoyi.common.websocket.constant.WebSocketConstants.LOGIN_USER_KEY;

/**
 * @author lyt
 * @since 2022/10/31
 */
@Slf4j
public class PlusWebSocketHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get( LOGIN_USER_KEY );
        WebSocketSessionHolder.addSession( loginUser.getUserId(), session );
        log.info( "[connect] sessionId: {},userId:{},userType:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType() );
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get( LOGIN_USER_KEY );
        //心跳监测的回复
        if (message instanceof PingMessage) {

            log.info( "[ping] sessionId: {},userId:{},userType:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType() );
            WebSocketUtil.sendPongMessage( session );
        } else if (message instanceof TextMessage) {
            log.info( "PlusWebSocketHandler, 连接：" + session.getId() + "，已收到消息:" + message.getPayload() );
            WebSocketMessageDto webSocketMessageDto = new WebSocketMessageDto();
            webSocketMessageDto.setMessage( message.getPayload() );
            webSocketMessageDto.setUserId( loginUser.getUserId() );
            //发布订阅消息
            WebSocketUtil.publishMessage( webSocketMessageDto );
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get( LOGIN_USER_KEY );
        WebSocketSessionHolder.removeSession( loginUser.getUserId() );
        log.info( "[disconnect] sessionId: {},userId:{},userType:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType() );
    }

    // 支持分片消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
