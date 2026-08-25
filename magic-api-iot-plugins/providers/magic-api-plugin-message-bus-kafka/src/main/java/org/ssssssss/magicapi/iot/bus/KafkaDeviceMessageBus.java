package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class KafkaDeviceMessageBus implements DeviceMessageBus {
    private final KafkaTemplate<String, String> kafka;
    private final ConsumerFactory<String, String> consumerFactory;
    private final ObjectMapper mapper;
    private final String topic;
    private final int retries;
    private final Duration retryDelay;
    private final int maxPending;

    public KafkaDeviceMessageBus(KafkaTemplate<String, String> kafka, ConsumerFactory<String, String> consumerFactory,
                                 ObjectMapper mapper, String topic, int retries, Duration retryDelay, int maxPending) {
        this.kafka = kafka;
        this.consumerFactory = consumerFactory;
        this.mapper = mapper;
        this.topic = topic;
        this.retries = retries;
        this.retryDelay = retryDelay;
        this.maxPending = maxPending;
    }

    @Override
    public void publish(DeviceMessage message) {
        try {
            kafka.send(topic, message.device().routingKey(), mapper.writeValueAsString(message));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to publish device message", exception);
        }
    }

    @Override
    public Subscription subscribe(String subscriberId, Consumer<DeviceMessage> consumer) {
        Objects.requireNonNull(subscriberId, "subscriberId");
        Objects.requireNonNull(consumer, "consumer");
        ContainerProperties properties = new ContainerProperties(topic);
        properties.setGroupId("iot-" + subscriberId);
        properties.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        java.util.Properties consumerProperties = new java.util.Properties();
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.setKafkaConsumerProperties(consumerProperties);
        Semaphore pending = new Semaphore(maxPending);
        KafkaMessageListenerContainer<String, String> container = new KafkaMessageListenerContainer<>(consumerFactory, properties);
        container.setupMessageListener((org.springframework.kafka.listener.AcknowledgingMessageListener<String, String>)
            (record, acknowledgment) -> {
                if (!pending.tryAcquire()) {
                    publishDeadLetter(record.key(), record.value(), new IllegalStateException("Kafka consumer backpressure limit reached"));
                    acknowledgment.acknowledge();
                    return;
                }
                try {
                    consumeWithRetry(record.value(), consumer);
                    acknowledgment.acknowledge();
                } catch (Exception exception) {
                    publishDeadLetter(record.key(), record.value(), exception);
                    acknowledgment.acknowledge();
                } finally {
                    pending.release();
                }
            });
        container.start();
        return () -> {
            container.stop();
            container.destroy();
        };
    }

    private void consumeWithRetry(String value, Consumer<DeviceMessage> consumer) throws Exception {
        Exception failure = null;
        for (int attempt = 0; attempt <= retries; attempt++) {
            try {
                consumer.accept(mapper.readValue(value, DeviceMessage.class));
                return;
            } catch (Exception exception) {
                failure = exception;
                if (attempt < retries && !retryDelay.isZero()) Thread.sleep(retryDelay.toMillis());
            }
        }
        throw failure;
    }

    private void publishDeadLetter(String key, String value, Exception cause) {
        kafka.send(topic + ".DLT", key, value).whenComplete((result, sendFailure) -> {
            if (sendFailure != null) cause.addSuppressed(sendFailure);
        });
    }
}
