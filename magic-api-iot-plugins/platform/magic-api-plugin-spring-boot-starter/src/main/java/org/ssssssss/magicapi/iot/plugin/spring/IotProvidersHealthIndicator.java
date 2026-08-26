package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;

import java.util.LinkedHashMap;
import java.util.Map;

public class IotProvidersHealthIndicator implements HealthIndicator {
    private final ProviderHealthCatalog catalog;

    public IotProvidersHealthIndicator(ProviderHealthCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public Health health() {
        var snapshots = catalog.snapshots();
        PluginHealth.Status overall = snapshots.stream()
            .map(snapshot -> snapshot.health().status())
            .reduce(PluginHealth.Status.UP, IotProvidersHealthIndicator::worst);
        Map<String, Object> providers = new LinkedHashMap<>();
        snapshots.forEach(snapshot -> providers.put(snapshot.providerType() + ":" + snapshot.providerId(), Map.of(
            "status", snapshot.health().status().name(),
            "message", snapshot.health().message(),
            "details", snapshot.health().details())));
        return Health.status(overall.name())
            .withDetail("providerCount", snapshots.size())
            .withDetail("providers", providers)
            .build();
    }

    private static PluginHealth.Status worst(PluginHealth.Status left, PluginHealth.Status right) {
        return severity(left) >= severity(right) ? left : right;
    }

    private static int severity(PluginHealth.Status status) {
        return switch (status) {
            case UP -> 0;
            case UNKNOWN -> 1;
            case DEGRADED -> 2;
            case DOWN -> 3;
        };
    }
}
