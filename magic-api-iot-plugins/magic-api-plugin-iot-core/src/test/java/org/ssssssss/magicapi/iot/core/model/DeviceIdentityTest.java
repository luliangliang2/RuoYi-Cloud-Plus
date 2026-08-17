package org.ssssssss.magicapi.iot.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeviceIdentityTest {

    @Test
    void buildsStableRoutingKey() {
        assertEquals("tenant/product/device", new DeviceIdentity("tenant", "product", "device").routingKey());
    }

    @Test
    void rejectsBlankParts() {
        assertThrows(IllegalArgumentException.class, () -> new DeviceIdentity("tenant", "", "device"));
    }
}

