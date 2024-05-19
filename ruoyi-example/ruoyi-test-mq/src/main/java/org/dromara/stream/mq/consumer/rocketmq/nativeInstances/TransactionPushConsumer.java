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
public class TransactionPushConsumer {

    public void pushConsumerTest(String topic,String consumerGroup) throws Exception {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        String endpoints = "192.168.1.13:8081";
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
            .setEndpoints(endpoints)
            // On some Windows platforms, you may encounter SSL compatibility issues. Try turning off the SSL option in
            // client configuration to solve the problem please if SSL is not essential.
            // .enableSsl(false)
            //.setCredentialProvider(sessionCredentialsProvider)
            .build();
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        /*String consumerGroup = "transaction_group";
        String topic = "transaction_topic";*/
        // In most case, you don't need to create too many consumers, singleton pattern is recommended.
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
            .setClientConfiguration(clientConfiguration)
            // Set the consumer group name.
            .setConsumerGroup(consumerGroup)
            // Set the subscription for the consumer.
            .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
            .setMessageListener(messageView -> {
                // Handle the received message and return consume result.
                log.info("事务消费Consume message={}", messageView);
                return ConsumeResult.SUCCESS;
            })
            .build();
        // Block the main thread, no need for production environment.
        Thread.sleep(10000);
        // Close the push consumer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        pushConsumer.close();
    }
}
