package org.ssssssss.magicapi.iot.plugin.api;

public interface ProviderHealthIndicator {
    String providerId();

    String providerType();

    PluginHealth health();
}
