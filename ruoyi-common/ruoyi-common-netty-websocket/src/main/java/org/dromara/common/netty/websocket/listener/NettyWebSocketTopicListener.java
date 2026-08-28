package org.dromara.common.netty.websocket.listener;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.netty.websocket.config.properties.NettyWebSocketProperties;
import org.dromara.common.netty.websocket.core.NettyWebSocketMessagePublisher;
import org.dromara.common.netty.websocket.dto.NettyWebSocketMessageDto;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

/**
 * Netty WebSocket Redis 主题监听器。
 *
 * @author ruoyi
 */
@Slf4j
public class NettyWebSocketTopicListener implements ApplicationRunner, Ordered {

    private final NettyWebSocketProperties properties;

    private final NettyWebSocketMessagePublisher publisher;

    public NettyWebSocketTopicListener(NettyWebSocketProperties properties, NettyWebSocketMessagePublisher publisher) {
        this.properties = properties;
        this.publisher = publisher;
    }

    @Override
    public void run(ApplicationArguments args) {
        RedisUtils.subscribe(properties.getRedisTopic(), NettyWebSocketMessageDto.class, publisher::handle);
        log.info("Netty WebSocket topic listener initialized: {}", properties.getRedisTopic());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
