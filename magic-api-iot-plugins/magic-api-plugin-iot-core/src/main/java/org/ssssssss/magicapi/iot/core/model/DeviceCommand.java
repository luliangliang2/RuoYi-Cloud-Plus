package org.ssssssss.magicapi.iot.core.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record DeviceCommand(
    String commandId,
    DeviceIdentity device,
    String action,
    Object payload,
    int maxAttempts,
    Duration timeout,
    Instant createdAt,
    Map<String, String> metadata
) {
    public DeviceCommand {
        commandId = commandId == null || commandId.isBlank() ? UUID.randomUUID().toString() : commandId;
        Objects.requireNonNull(device, "device");
        action = Objects.requireNonNull(action, "action");
        if (action.isBlank()) throw new IllegalArgumentException("action must not be blank");
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts must be positive");
        timeout = timeout == null ? Duration.ofSeconds(30) : timeout;
        createdAt = createdAt == null ? Instant.now() : createdAt;
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}

