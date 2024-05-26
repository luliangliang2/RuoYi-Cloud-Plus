package org.dromara.stream.mq.producer.rocketMq.SpringInstances;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author xbhog
 */
@Slf4j
@Component
public class SpringRocketTransactionProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendTransactionalMessage(String topic, String msg) {
        log.info("【生产者】start sendTransMessage hashKey:{}",msg);

        Message message =new Message();
        message.setBody("this is tx message".getBytes());
        TransactionSendResult result=rocketMQTemplate.sendMessageInTransaction(topic,
            MessageBuilder.withPayload(message).build(), msg);

        //发送状态
        String sendStatus = result.getSendStatus().name();
        // 本地事务执行状态
        String localTxState = result.getLocalTransactionState().name();
        log.info("【生产者】send tx message sendStatus:{},localTXState:{}",sendStatus,localTxState);
    }
}
