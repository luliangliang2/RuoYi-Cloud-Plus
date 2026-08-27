package org.ssssssss.magicapi.iot.plugin.spring;

import org.ssssssss.magicapi.iot.core.spi.DeviceAuthenticator;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;
import org.ssssssss.magicapi.iot.core.spi.HandshakeProviderRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginServiceRegistry;

import java.util.List;
import java.util.Optional;

final class DefaultHandshakeProviderRegistry implements HandshakeProviderRegistry {
    private final PluginServiceRegistry services;

    DefaultHandshakeProviderRegistry(PluginServiceRegistry services) {
        this.services = services;
    }

    @Override public Optional<DeviceHandshakeProvider> handshake(String providerId) {
        return services.find(DeviceHandshakeProvider.class, providerId);
    }

    @Override public Optional<DeviceAuthenticator> authenticator(String providerId) {
        return services.find(DeviceAuthenticator.class, providerId);
    }

    @Override public List<DeviceHandshakeProvider> handshakes() {
        return services.services(DeviceHandshakeProvider.class);
    }

    @Override public List<DeviceAuthenticator> authenticators() {
        return services.services(DeviceAuthenticator.class);
    }
}
