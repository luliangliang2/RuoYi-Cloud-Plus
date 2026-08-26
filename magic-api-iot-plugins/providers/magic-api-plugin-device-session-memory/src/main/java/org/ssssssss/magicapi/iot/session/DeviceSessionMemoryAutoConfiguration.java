package org.ssssssss.magicapi.iot.session;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.device-session", name = "type", havingValue = "memory")
public class DeviceSessionMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SessionRepository.class)
    SessionRepository sessionRepository() {
        return new InMemorySessionRepository();
    }

    @Bean
    ProviderHealthIndicator memoryDeviceSessionHealth() {
        return new ProviderHealthIndicator() {
            public String providerId() { return "memory"; }
            public String providerType() { return "device-session"; }
            public PluginHealth health() { return PluginHealth.up(); }
        };
    }
}
