package org.ssssssss.magicapi.iot.ops;

import java.time.Instant;

public record DebugMessage(String nodeId, String deviceId, String direction, byte[] payload, Instant timestamp) {
    public DebugMessage { payload = payload == null ? new byte[0] : payload.clone(); }
    @Override public byte[] payload() { return payload.clone(); }
}

