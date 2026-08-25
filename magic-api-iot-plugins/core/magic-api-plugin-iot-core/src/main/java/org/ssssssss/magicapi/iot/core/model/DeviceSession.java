package org.ssssssss.magicapi.iot.core.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public record DeviceSession(
    String sessionId,
    DeviceIdentity device,
    String gatewayNodeId,
    String transport,
    String remoteAddress,
    Instant connectedAt,
    Instant lastSeenAt,
    Map<String, String> attributes
) {
    public DeviceSession {
        Objects.requireNonNull(sessionId, "sessionId");
        Objects.requireNonNull(device, "device");
        Objects.requireNonNull(gatewayNodeId, "gatewayNodeId");
        Objects.requireNonNull(transport, "transport");
        connectedAt = connectedAt == null ? Instant.now() : connectedAt;
        lastSeenAt = lastSeenAt == null ? connectedAt : lastSeenAt;
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}

