package org.dromara.stream.mq.consumer.rocketmq.SpringInstances;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
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
    messageModel = MessageModel.CLUSTERING
)
public class SpringRocketTransactionListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("【消费者】received spring rocketmq transaction messages  ======》 {}", message);
    }
}
