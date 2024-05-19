package org.dromara.stream.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.stream.mq.consumer.rocketmq.nativeInstances.NormalPushConsumer;
import org.dromara.stream.mq.consumer.rocketmq.nativeInstances.TransactionPushConsumer;
import org.dromara.stream.mq.producer.kafkaMq.NormalKafkaProducer;
import org.dromara.stream.mq.producer.rabbitMq.NormalRabbitProducer;
import org.dromara.stream.mq.producer.rocketMq.nativeInstances.NormalMessageSyncProducer;
import org.dromara.stream.mq.producer.rocketMq.nativeInstances.TransactionMessageProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    private RabbitTemplate rabbitTemplate;

    @Resource
    private NormalMessageSyncProducer syncProducer;

    @Resource
    private TransactionMessageProducer transactionMessageProducer;

    @Resource
    private TransactionPushConsumer transactionPushConsumer;

    @Resource
    private NormalPushConsumer pushConsumer;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private NormalKafkaProducer normalKafkaProducer;

    /**
     * rabbit普通消息的处理
     */
    @GetMapping("/rabbitMsg/send")
    public String sendMq() {
        return normalRabbitProducer.sendMq("hello rabbitMQ");
    }

    /**
     * rabbit延迟队列类型：类似生产者
     */
    @GetMapping("/rabbitMsg/sendTTL/")
    public void sendMessage() {
        String message = "Hello ttl RabbitMsg";
        String ttl = "2000";
        log.info("当前时间：{} 发送一条信息给队列{},ttl为：{}", new Date().toString(), message, ttl);
        rabbitTemplate.convertAndSend("X", "XC", "TTL为用户设置的队列：" + message, (msg -> {
            //发送消息 并设置TTL
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        }));
    }

    /**
     * rockerMQ原生实例
     * 需要手动创建相关的Topic和group
     * @throws Exception
     */
    @GetMapping("/rocketMq/send")
    public void sendRockerMq() throws Exception {
        syncProducer.sendMessage();
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
        rocketMQTemplate.convertAndSend("test", "hello RocketMQ");
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
