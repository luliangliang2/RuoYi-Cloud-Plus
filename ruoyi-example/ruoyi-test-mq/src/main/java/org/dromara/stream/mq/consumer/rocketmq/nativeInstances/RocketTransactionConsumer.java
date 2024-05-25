package org.dromara.stream.mq.consumer.rocketmq.nativeInstances;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author xbhog
 * @date 2024/05/19 11:44
 **/
@Slf4j
@Component
public class RocketTransactionConsumer {

    public void pushConsumerTest(String topic,String consumerGroup) throws Exception {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        String endpoints = "192.168.1.13:8081";
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
            .setEndpoints(endpoints)
             /*在某些 Windows 平台上，您可能会遇到 SSL 兼容性问题。尝试关闭 SSL 选项
             如果SSL不是必需的，请客户端配置来解决问题。*/
            // .enableSsl(false)
            //.setCredentialProvider(sessionCredentialsProvider)
            .build();
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);

        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
            .setClientConfiguration(clientConfiguration)
            // 设置使用者组名称。
            .setConsumerGroup(consumerGroup)
            // 为使用者设置订阅。
            .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
            .setMessageListener(messageView -> {
                // 处理收到的消息并返回消费结果。
                log.info("【消费者】Transaction Consume message={}", messageView);
                return ConsumeResult.SUCCESS;
            })
            .build();
        Thread.sleep(10000);
        pushConsumer.close();
    }
}
