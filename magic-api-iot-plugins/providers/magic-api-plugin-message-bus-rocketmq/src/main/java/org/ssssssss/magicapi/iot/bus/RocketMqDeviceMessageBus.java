package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

public class RocketMqDeviceMessageBus implements DeviceMessageBus {
    private final RocketMQTemplate template;
    private final ObjectMapper mapper;
    private final String nameServer;
    private final String topic;
    private final int maxRetries;

    public RocketMqDeviceMessageBus(RocketMQTemplate template, ObjectMapper mapper,
                                    String nameServer, String topic, int maxRetries) {
        this.template = template;
        this.mapper = mapper;
        this.nameServer = nameServer;
        this.topic = topic;
        this.maxRetries = maxRetries;
    }

    @Override
    public void publish(DeviceMessage message) {
        try {
            template.syncSend(topic, mapper.writeValueAsString(message));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to publish RocketMQ device message", exception);
        }
    }

    @Override
    public Subscription subscribe(String subscriberId, Consumer<DeviceMessage> consumer) {
        Objects.requireNonNull(subscriberId, "subscriberId");
        Objects.requireNonNull(consumer, "consumer");
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer("iot-" + subscriberId);
        pushConsumer.setNamesrvAddr(nameServer);
        pushConsumer.setMaxReconsumeTimes(maxRetries);
        try {
            pushConsumer.subscribe(topic, "*");
            pushConsumer.registerMessageListener((org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently)
                (messages, context) -> {
                    try {
                        for (var message : messages) {
                            consumer.accept(mapper.readValue(new String(message.getBody(), StandardCharsets.UTF_8), DeviceMessage.class));
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    } catch (Exception exception) {
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                });
            pushConsumer.start();
        } catch (Exception exception) {
            pushConsumer.shutdown();
            throw new IllegalStateException("Failed to start RocketMQ subscriber: " + subscriberId, exception);
        }
        return pushConsumer::shutdown;
    }
}
