package org.ssssssss.magicapi.iot.script.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.script.ScriptRegistry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(EtcdScriptRegistryProperties.class)
@ConditionalOnProperty(prefix = "iot.script.registry", name = "type", havingValue = "etcd")
public class EtcdScriptRegistryAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(ScriptRegistry.class)
    EtcdScriptRegistry scriptRegistry(EtcdScriptRegistryProperties properties, ObjectMapper mapper) {
        properties.validate();
        var builder = Client.builder().endpoints(properties.getEndpoints().toArray(String[]::new));
        if (properties.getUsername() != null && !properties.getUsername().isBlank()) {
            builder.user(ByteSequence.from(properties.getUsername(), StandardCharsets.UTF_8));
            builder.password(ByteSequence.from(properties.getPassword(), StandardCharsets.UTF_8));
        }
        return new EtcdScriptRegistry(builder.build(), mapper, properties);
    }

    @Bean
    ProviderHealthIndicator etcdScriptRegistryHealth(EtcdScriptRegistry registry, EtcdScriptRegistryProperties properties,
                                                     org.springframework.core.env.Environment environment) {
        Duration cache = environment.getProperty("iot.health.cache-ttl", Duration.class, Duration.ofSeconds(10));
        Duration timeout = environment.getProperty("iot.health.timeout", Duration.class, Duration.ofSeconds(3));
        return new ProbeProviderHealthIndicator("script-registry", "etcd", cache, timeout,
                () -> registry.isAvailable() ? ProbeProviderHealthIndicator.up(Map.of("endpoints", properties.getEndpoints(), "rootPrefix", properties.getRootPrefix()))
                        : new PluginHealth(PluginHealth.Status.DOWN, "etcd script registry is unavailable", Map.of()));
    }
}
