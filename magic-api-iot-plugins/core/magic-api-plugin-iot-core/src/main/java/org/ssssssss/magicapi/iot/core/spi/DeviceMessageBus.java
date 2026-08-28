package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;

import java.util.function.Consumer;

public interface DeviceMessageBus {

    void publish(DeviceMessage message);

    Subscription subscribe(String subscriberId, Consumer<DeviceMessage> consumer);

    interface Subscription extends AutoCloseable {
        @Override
        void close();
    }
}

