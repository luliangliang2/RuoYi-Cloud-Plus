package org.ssssssss.magicapi.iot.ops;

import java.time.Instant;

public record GatewaySnapshot(String nodeId, int activeSessions, long messagesIn, long messagesOut,
                              long decodeErrors, Instant capturedAt) {
}

