package org.ssssssss.magicapi.iot.cluster.nacos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NacosNodeRegistryTest {
    @Test void parsesGatewayAddress() {
        assertEquals(new NacosNodeRegistry.HostPort("10.0.0.8", 9218),
            NacosNodeRegistry.HostPort.parse("http://10.0.0.8:9218"));
        assertThrows(IllegalArgumentException.class, () -> NacosNodeRegistry.HostPort.parse("10.0.0.8"));
    }
}
