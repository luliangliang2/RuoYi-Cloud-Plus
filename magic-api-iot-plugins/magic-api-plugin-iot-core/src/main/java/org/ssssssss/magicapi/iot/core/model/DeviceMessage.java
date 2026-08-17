package org.ssssssss.magicapi.iot.core.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record DeviceMessage(
    String messageId,
    DeviceIdentity device,
    DeviceMessageType type,
    String protocol,
    Instant occurredAt,
    Long sequence,
    Object payload,
    Map<String, String> metadata
) {
    public DeviceMessage {
        messageId = messageId == null || messageId.isBlank() ? UUID.randomUUID().toString() : messageId;
        Objects.requireNonNull(device, "device");
        Objects.requireNonNull(type, "type");
        protocol = Objects.requireNonNull(protocol, "protocol");
        occurredAt = occurredAt == null ? Instant.now() : occurredAt;
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}

