package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceCommandStatus;

import java.time.Instant;

public record CommandResult(
    String commandId,
    DeviceCommandStatus status,
    int attempt,
    String detail,
    Instant updatedAt
) {
}

