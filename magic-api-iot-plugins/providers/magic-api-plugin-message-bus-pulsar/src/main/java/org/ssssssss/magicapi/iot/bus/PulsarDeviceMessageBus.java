package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.*;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class PulsarDeviceMessageBus implements DeviceMessageBus, AutoCloseable {
    private final PulsarClient client;
    private final Producer<String> producer;
    private final ObjectMapper mapper;
    private final String topic;
    private final int maxRetries;
    private final long retryDelayMs;
    private final int receiverQueueSize;

    public PulsarDeviceMessageBus(PulsarClient client, ObjectMapper mapper, String topic,
                                  int maxRetries, long retryDelayMs, int receiverQueueSize) throws PulsarClientException {
        this.client = client;
        this.mapper = mapper;
        this.topic = topic;
        this.maxRetries = maxRetries;
        this.retryDelayMs = retryDelayMs;
        this.receiverQueueSize = receiverQueueSize;
        this.producer = client.newProducer(Schema.STRING).topic(topic).create();
    }

    @Override
    public void publish(DeviceMessage message) {
        try {
            producer.newMessage().key(message.device().routingKey()).value(mapper.writeValueAsString(message)).send();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to publish Pulsar device message", exception);
        }
    }

    @Override
    public Subscription subscribe(String subscriberId, Consumer<DeviceMessage> handler) {
        Objects.requireNonNull(subscriberId, "subscriberId");
        Objects.requireNonNull(handler, "handler");
        String subscriptionName = "iot-" + subscriberId;
        try {
            org.apache.pulsar.client.api.Consumer<String> consumer = client.newConsumer(Schema.STRING)
                .topic(topic).subscriptionName(subscriptionName).subscriptionType(SubscriptionType.Shared)
                .receiverQueueSize(receiverQueueSize).negativeAckRedeliveryDelay(retryDelayMs, TimeUnit.MILLISECONDS)
                .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(maxRetries)
                    .deadLetterTopic(topic + "-DLQ").build()).subscribe();
            AtomicBoolean running = new AtomicBoolean(true);
            ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r, "iot-pulsar-" + subscriberId);
                thread.setDaemon(true);
                return thread;
            });
            executor.execute(() -> consumeLoop(consumer, handler, running));
            return () -> {
                running.set(false);
                executor.shutdownNow();
                try { consumer.close(); } catch (PulsarClientException ignored) { }
            };
        } catch (PulsarClientException exception) {
            throw new IllegalStateException("Failed to start Pulsar subscriber: " + subscriberId, exception);
        }
    }

    private void consumeLoop(org.apache.pulsar.client.api.Consumer<String> consumer,
                             Consumer<DeviceMessage> handler, AtomicBoolean running) {
        while (running.get()) {
            try {
                Message<String> message = consumer.receive(1, TimeUnit.SECONDS);
                if (message == null) continue;
                try {
                    handler.accept(mapper.readValue(message.getValue(), DeviceMessage.class));
                    consumer.acknowledge(message);
                } catch (Exception exception) {
                    consumer.negativeAcknowledge(message);
                }
            } catch (PulsarClientException exception) {
                if (running.get()) throw new IllegalStateException("Pulsar consumer failed", exception);
            }
        }
    }

    @Override public void close() throws Exception { producer.close(); }
}
