package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceCommand;
import org.ssssssss.magicapi.iot.core.model.DeviceCommandStatus;

import java.util.Optional;

public interface CommandGateway {

    CommandResult submit(DeviceCommand command);

    Optional<CommandResult> find(String commandId);

    CommandResult acknowledge(String commandId, DeviceCommandStatus status, String detail);
}

