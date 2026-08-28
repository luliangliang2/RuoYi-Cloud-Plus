package org.ssssssss.magicapi.iot.bus;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class InMemoryDeviceMessageBus implements DeviceMessageBus {

    private final ConcurrentHashMap<String, Consumer<DeviceMessage>> subscribers = new ConcurrentHashMap<>();

    @Override
    public void publish(DeviceMessage message) {
        subscribers.values().forEach(consumer -> consumer.accept(message));
    }

    @Override
    public Subscription subscribe(String subscriberId, Consumer<DeviceMessage> consumer) {
        Objects.requireNonNull(subscriberId, "subscriberId");
        Objects.requireNonNull(consumer, "consumer");
        if (subscribers.putIfAbsent(subscriberId, consumer) != null) {
            throw new IllegalArgumentException("Duplicate subscriber: " + subscriberId);
        }
        return () -> subscribers.remove(subscriberId, consumer);
    }
}

