package org.dromara.stream.mq.consumer.rocketmq.nativeInstances;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author xbhog
 */
@Slf4j
@Component
public class RocketNormalConsumer {

    public void pushConsumerTest(String topic,String consumerGroup) throws Exception {
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        // 接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8081;xxx:8081
        String endpoint = "192.168.1.13:8081";
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
            .setEndpoints(endpoint)
            .build();
        // 订阅消息的过滤规则，表示订阅所有Tag的消息
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        // 为消费者指定所属的消费者分组，Group需要提前创建
        //String consumerGroup = "TestGroup";

        // 指定需要订阅哪个目标Topic，Topic需要提前创建
        //String topic = "TestTopic";
        // 初始化 PushConsumer，需要绑定消费者分组ConsumerGroup、通信参数以及订阅关系
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
            .setClientConfiguration(clientConfiguration)
            // 设置消费者分组
            .setConsumerGroup(consumerGroup)
            // 设置预绑定的订阅关系
            .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
            // 设置消费监听器
            .setMessageListener(messageView -> {
                // 处理消息并返回消费结果
                log.info("【消费者】consume message successfully, messageId={}", messageView.getMessageId());
                // 消息内容处理
                ByteBuffer body = messageView.getBody();
                String message = StandardCharsets.UTF_8.decode(body).toString();
                body.flip();
                log.info("【消费者】message body={}", message);
                return ConsumeResult.SUCCESS;
            }).build();
        Thread.sleep(5000);
        // 如果不需要再使用 PushConsumer，可关闭该实例。
        pushConsumer.close();
    }

}
