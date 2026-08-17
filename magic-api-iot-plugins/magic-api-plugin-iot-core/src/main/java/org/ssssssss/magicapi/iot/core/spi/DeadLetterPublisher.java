package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;

public interface DeadLetterPublisher {
    void publish(DeviceMessage message, Throwable cause);
}

