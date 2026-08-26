package org.ssssssss.magicapi.iot.plugin.spring;

import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProviderHealthCatalog {
    private final Map<String, ProviderHealthIndicator> indicators;

    public ProviderHealthCatalog(Collection<ProviderHealthIndicator> indicators) {
        try {
            this.indicators = indicators.stream().collect(Collectors.toUnmodifiableMap(
                ProviderHealthCatalog::key, Function.identity()));
        } catch (IllegalStateException exception) {
            throw new IllegalArgumentException("Duplicate provider health indicator", exception);
        }
    }

    public List<ProviderHealthSnapshot> snapshots() {
        return indicators.values().stream()
            .map(indicator -> new ProviderHealthSnapshot(
                indicator.providerType(), indicator.providerId(), indicator.health()))
            .sorted(Comparator.comparing(ProviderHealthSnapshot::providerType)
                .thenComparing(ProviderHealthSnapshot::providerId))
            .toList();
    }

    private static String key(ProviderHealthIndicator indicator) {
        return indicator.providerType() + ":" + indicator.providerId();
    }

    public record ProviderHealthSnapshot(String providerType, String providerId, PluginHealth health) {
    }
}
