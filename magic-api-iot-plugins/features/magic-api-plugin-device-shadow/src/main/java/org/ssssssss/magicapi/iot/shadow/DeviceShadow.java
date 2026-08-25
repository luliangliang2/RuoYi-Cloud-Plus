package org.ssssssss.magicapi.iot.shadow;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;

import java.time.Instant;
import java.util.Map;

public record DeviceShadow(DeviceIdentity device, Map<String, Object> reported, Map<String, Object> desired, long version, Instant updatedAt) {
    public DeviceShadow {
        reported = reported == null ? Map.of() : Map.copyOf(reported);
        desired = desired == null ? Map.of() : Map.copyOf(desired);
        updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }
}

