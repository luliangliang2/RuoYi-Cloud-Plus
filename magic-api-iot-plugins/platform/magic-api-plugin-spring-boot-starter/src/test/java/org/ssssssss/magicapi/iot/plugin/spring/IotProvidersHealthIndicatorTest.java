package org.ssssssss.magicapi.iot.plugin.spring;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IotProvidersHealthIndicatorTest {
    @Test void exposesWorstProviderStatus() {
        ProviderHealthCatalog catalog = new ProviderHealthCatalog(List.of(
            indicator("device-registry", "redis", PluginHealth.up()),
            indicator("message-bus", "kafka", PluginHealth.down("unreachable"))));

        var health = new IotProvidersHealthIndicator(catalog).health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals(2, health.getDetails().get("providerCount"));
    }

    private ProviderHealthIndicator indicator(String type, String id, PluginHealth health) {
        return new ProviderHealthIndicator() {
            public String providerId() { return id; }
            public String providerType() { return type; }
            public PluginHealth health() { return health; }
        };
    }
}
