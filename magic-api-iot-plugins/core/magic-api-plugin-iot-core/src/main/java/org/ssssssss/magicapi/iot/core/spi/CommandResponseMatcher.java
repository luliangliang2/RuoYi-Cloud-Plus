package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceCommand;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;

public interface CommandResponseMatcher {
    boolean matches(DeviceCommand command, DeviceMessage message);
}

