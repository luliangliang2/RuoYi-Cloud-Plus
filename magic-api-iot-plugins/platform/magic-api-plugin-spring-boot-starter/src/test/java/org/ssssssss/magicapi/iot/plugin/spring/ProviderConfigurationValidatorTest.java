package org.ssssssss.magicapi.iot.plugin.spring;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRuntimeException;

import static org.junit.jupiter.api.Assertions.*;

class ProviderConfigurationValidatorTest {
    private final ProviderConfigurationValidator validator = new ProviderConfigurationValidator();

    @Test void acceptsValidDistributedProductionProviders() {
        MockEnvironment environment = new MockEnvironment().withProperty("spring.profiles.active", "prod")
            .withProperty("iot.providers.device-registry.type", "redis")
            .withProperty("iot.providers.device-session.type", "redis")
            .withProperty("iot.providers.message-bus.type", "kafka")
            .withProperty("iot.providers.node-registry.type", "nacos")
            .withProperty("iot.providers.configuration-center.type", "etcd");
        environment.setActiveProfiles("prod");
        assertDoesNotThrow(() -> validator.validate(environment));
    }

    @Test void rejectsMemoryInProduction() {
        MockEnvironment environment = configured("memory", "redis", "kafka");
        environment.setActiveProfiles("prod");
        assertThrows(PluginRuntimeException.class, () -> validator.validate(environment));
    }

    @Test void rejectsProviderTypeAssignedToWrongContract() {
        MockEnvironment environment = configured("kafka", "redis", "kafka");
        assertThrows(PluginRuntimeException.class, () -> validator.validate(environment));
    }

    @Test void rejectsMissingProductionProvider() {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("production");
        assertThrows(PluginRuntimeException.class, () -> validator.validate(environment));
    }

    private MockEnvironment configured(String registry, String session, String bus) {
        return new MockEnvironment()
            .withProperty("iot.providers.device-registry.type", registry)
            .withProperty("iot.providers.device-session.type", session)
            .withProperty("iot.providers.message-bus.type", bus)
            .withProperty("iot.providers.node-registry.type", "nacos")
            .withProperty("iot.providers.configuration-center.type", "nacos");
    }
}
