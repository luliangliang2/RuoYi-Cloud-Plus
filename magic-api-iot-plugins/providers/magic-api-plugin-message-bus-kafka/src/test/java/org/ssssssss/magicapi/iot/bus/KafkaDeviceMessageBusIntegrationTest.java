package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.kafka.core.*;
import org.ssssssss.magicapi.iot.core.model.*;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "iot.integration.enabled", matches = "true")
class KafkaDeviceMessageBusIntegrationTest {

    @Test void publishesConsumesAndAcknowledgesDeviceMessage() throws Exception {
        String bootstrap = System.getProperty("iot.kafka.bootstrap", "10.211.55.4:9092");
        String suffix = UUID.randomUUID().toString();
        String topic = "iot-test-device-messages-" + suffix;
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class));
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"));
        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);
        KafkaDeviceMessageBus bus = new KafkaDeviceMessageBus(template, consumerFactory,
            new ObjectMapper().findAndRegisterModules(), topic, 1, Duration.ofMillis(50), 10);
        CountDownLatch received = new CountDownLatch(1);
        var subscription = bus.subscribe("integration-" + suffix, message -> received.countDown());
        try {
            Thread.sleep(1000);
            bus.publish(new DeviceMessage(null, new DeviceIdentity("iot-test", suffix),
                DeviceMessageType.PROPERTY_REPORT, "integration", null, null, Map.of("value", 1), Map.of()));
            assertTrue(received.await(10, TimeUnit.SECONDS), "Kafka consumer did not receive the published message");
        } finally {
            subscription.close();
            template.destroy();
            producerFactory.destroy();
        }
    }

    @Test void retriesThenPublishesToDeadLetterTopic() throws Exception {
        String bootstrap = System.getProperty("iot.kafka.bootstrap", "10.211.55.4:9092");
        String suffix = UUID.randomUUID().toString();
        String topic = "iot-test-device-failure-" + suffix;
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class));
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"));
        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);
        KafkaDeviceMessageBus bus = new KafkaDeviceMessageBus(template, consumerFactory,
            new ObjectMapper().findAndRegisterModules(), topic, 2, Duration.ofMillis(20), 10);
        AtomicInteger attempts = new AtomicInteger();
        var subscription = bus.subscribe("failure-" + suffix, message -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("expected integration failure");
        });
        Map<String, Object> dltConfig = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap,
            ConsumerConfig.GROUP_ID_CONFIG, "dlt-verifier-" + suffix,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        try (KafkaConsumer<String, String> dltConsumer = new KafkaConsumer<>(dltConfig)) {
            dltConsumer.subscribe(java.util.List.of(topic + ".DLT"));
            Thread.sleep(1000);
            bus.publish(new DeviceMessage(null, new DeviceIdentity("iot-test", suffix),
                DeviceMessageType.EVENT_REPORT, "integration", null, null, Map.of("failed", true), Map.of()));
            boolean deadLetterReceived = false;
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (!deadLetterReceived && System.nanoTime() < deadline) {
                deadLetterReceived = !dltConsumer.poll(Duration.ofMillis(500)).isEmpty();
            }
            assertTrue(deadLetterReceived, "Failed message was not published to the DLT");
            assertTrue(attempts.get() >= 3, "Consumer was not retried the configured number of times");
        } finally {
            subscription.close();
            template.destroy();
            producerFactory.destroy();
        }
    }
}
