package org.dromara.stream.mq.producer.rocketMq.nativeInstances;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author xbhog
 * @description RocketMQ 普通消息同步发送。rocketMQ5x需要打开代理
 */
@Component
public class RocketNormalProducer {
    private static final Logger log = LoggerFactory.getLogger(RocketNormalProducer.class);

    private RocketNormalProducer() {
    }

    public void sendMessage() throws ClientException, IOException {
        // 接入点地址，需要设置成 Proxy 的地址和端口列表，一般是xxx:8081
        String endpoint = "192.168.1.13:8081";
        // 消息发送的目标Topic名称，需要提前创建。
        String topic = "TestTopic";
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoint);
        ClientConfiguration configuration = builder.build();

        // 初始化Producer时需要设置通信配置以及预绑定的Topic
        Producer producer = provider.newProducerBuilder()
            .setTopics(topic)
            .setClientConfiguration(configuration)
            .build();

        // 普通消息发送
        Message message = provider.newMessageBuilder()
            .setTopic(topic)
            //.setMessageGroup("TestGroup")
            // 设置消息索引键，可根据关键字精确查找某条消息
            .setKeys("messageKey")
            // 设置消息Tag，用于消费端根据指定Tag过滤消息
            .setTag("messageTag")
            // 消息内容实体（byte[]）
            .setBody("hello rocketMQ".getBytes())
            .build();
        try {
            // 发送消息，需要关注发送结果，并捕获失败等异常。
            SendReceipt sendReceipt = producer.send(message);
            log.info("【生产者】send message successfully, messageId={}", sendReceipt.getMessageId());
        } catch (ClientException e) {
            log.error("【生产者】failed to send message", e);
        }
        // 关闭
        producer.close();
    }
}
