package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;

import java.util.Map;

public record RegisteredDevice(
    DeviceIdentity identity,
    boolean enabled,
    String protocolId,
    Map<String, String> capabilities,
    long version
) {
    public RegisteredDevice {
        capabilities = capabilities == null ? Map.of() : Map.copyOf(capabilities);
    }
}

