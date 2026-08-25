package org.ssssssss.magicapi.iot.core.model;

import java.util.Map;

public record ProtocolContext(
    String transport,
    String remoteAddress,
    DeviceIdentity device,
    Map<String, String> attributes
) {
    public ProtocolContext {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}

