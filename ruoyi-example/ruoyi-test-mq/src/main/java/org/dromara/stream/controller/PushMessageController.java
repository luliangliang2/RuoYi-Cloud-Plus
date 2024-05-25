package org.dromara.stream.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.stream.mq.consumer.rocketmq.nativeInstances.RocketNormalConsumer;
import org.dromara.stream.mq.consumer.rocketmq.nativeInstances.RocketTransactionConsumer;
import org.dromara.stream.mq.producer.kafkaMq.KafkaNormalProducer;
import org.dromara.stream.mq.producer.rabbitMq.DelayRabbitProducer;
import org.dromara.stream.mq.producer.rabbitMq.NormalRabbitProducer;
import org.dromara.stream.mq.producer.rocketMq.SpringRocketNormalProducer;
import org.dromara.stream.mq.producer.rocketMq.nativeInstances.RocketNormalProducer;
import org.dromara.stream.mq.producer.rocketMq.nativeInstances.RocketTransactionProducer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xbhog
 */
@Slf4j
@RestController
@RequestMapping("push/message")
public class PushMessageController {

    @Resource
    private NormalRabbitProducer normalRabbitProducer;

    @Resource
    private DelayRabbitProducer delayRabbitProducer;

    @Resource
    private RocketNormalProducer rocketNormalProducer;

    @Resource
    private RocketTransactionProducer transactionMessageProducer;

    @Resource
    private RocketTransactionConsumer transactionPushConsumer;

    @Resource
    private SpringRocketNormalProducer springRocketNormalProducer;


    @Resource
    private RocketNormalConsumer pushConsumer;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private KafkaNormalProducer normalKafkaProducer;

    /**
     * rabbit普通消息的处理
     */
    @GetMapping("/rabbitMsg/sendNormal")
    public void sendMq() {
        normalRabbitProducer.sendMq("hello normal RabbitMsg");
    }

    /**
     * rabbit延迟队列类型：类似生产者
     */
    @GetMapping("/rabbitMsg/sendDelay")
    public void sendMessage() {
        delayRabbitProducer.sendDelayMessage("Hello ttl RabbitMsg");
    }

    /**
     * rockerMQ原生实例
     * 需要手动创建相关的Topic和group
     * @throws Exception
     */
    @GetMapping("/rocketMq/send")
    public void sendRockerMq() throws Exception {
        rocketNormalProducer.sendMessage();
        pushConsumer.pushConsumerTest("TestTopic","TestGroup");
    }
    @GetMapping("/rocketMq/transactionMsg")
    public void sendRockerMqTransactionMsg() throws Exception {
        transactionMessageProducer.sendTransactionMessage();
        transactionPushConsumer.pushConsumerTest("transaction_topic","transaction_group");
    }
    /**
     * 集成SpringBoot
     */
    @GetMapping("/rocketMq/normalMsg")
    public void sendNormalMsg() {
        springRocketNormalProducer.springNormalMessage();
    }

    @GetMapping("/rocketMq/normalMsg/transactionMsg")
    public void sendTransactionMsg() {
        String destination = "transaction_topic:tx";
        for (int i = 1; i < 5; i++) {
            Message<String> message = MessageBuilder.withPayload(String.format("事务消息%s", i))
                .setHeader("orderId", i)
                .build();
            Map<String, Object> params = new HashMap<>();
            // 发送事务消息
            TransactionSendResult res = rocketMQTemplate.sendMessageInTransaction(destination, message, params);
            log.info("msgId = {} , sendStatus = {}", res.getMsgId(), res.getSendStatus());
        }
    }
    /**
     * kafkaSpringboot集成
     */
    @GetMapping("/kafkaMsg/send")
    public void sendKafkaMsg(){
        normalKafkaProducer.sendKafkaMsg();
    }
}
