package org.dromara.stream.mq.consumer.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author xbhog
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "test", consumerGroup = "test_consumer")
public class SpringRocketMQMessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("接收到Spring RocketMQ消息[topic={}] ======> {}", "test", s);
    }
}
