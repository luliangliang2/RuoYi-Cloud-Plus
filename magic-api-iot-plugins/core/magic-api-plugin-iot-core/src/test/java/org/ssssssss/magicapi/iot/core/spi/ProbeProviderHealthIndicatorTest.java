package org.ssssssss.magicapi.iot.core.spi;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProbeProviderHealthIndicatorTest {
    @Test void cachesSuccessfulProbeResult() {
        AtomicInteger calls = new AtomicInteger();
        var indicator = new ProbeProviderHealthIndicator("message-bus", "test",
            Duration.ofMinutes(1), Duration.ofSeconds(1), () -> {
                calls.incrementAndGet();
                return Map.of("target", "broker");
            });

        assertEquals(PluginHealth.Status.UP, indicator.health().status());
        assertEquals(PluginHealth.Status.UP, indicator.health().status());
        assertEquals(1, calls.get());
    }

    @Test void reportsProbeFailureAsDown() {
        var indicator = new ProbeProviderHealthIndicator("message-bus", "test",
            Duration.ofMinutes(1), Duration.ofSeconds(1), () -> {
                throw new IllegalStateException("broker unavailable");
            });

        assertEquals(PluginHealth.Status.DOWN, indicator.health().status());
        assertEquals("broker unavailable", indicator.health().message());
    }
}
