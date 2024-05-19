package org.dromara.stream.mq.consumer.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author xbhog
 * @date 2024/05/19 16:06
 **/
@Slf4j
@Component
@RocketMQMessageListener(
    topic = "transaction_topic",
    consumerGroup = "test_consumer",
    selectorExpression = "tx"
)
public class SpringRocketMQMessageTransactionListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("接收到Spring RocketMQ事务消息[topic={}] ======> {}", "test", message);
    }
}
