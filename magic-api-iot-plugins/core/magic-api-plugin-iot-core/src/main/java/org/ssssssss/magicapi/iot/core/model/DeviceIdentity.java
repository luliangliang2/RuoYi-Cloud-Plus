package org.ssssssss.magicapi.iot.core.model;

import java.util.Objects;

public record DeviceIdentity(String productId, String deviceId) {

    public DeviceIdentity {
        productId = requireText(productId, "productId");
        deviceId = requireText(deviceId, "deviceId");
    }

    public String routingKey() {
        return productId + "/" + deviceId;
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
