package org.ssssssss.magicapi.iot.cluster.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(EtcdNodeRegistryProperties.class)
@ConditionalOnProperty(prefix = "iot.providers.node-registry", name = "type", havingValue = "etcd")
public class EtcdNodeRegistryAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(NodeRegistry.class)
    EtcdNodeRegistry etcdNodeRegistry(EtcdNodeRegistryProperties properties, ObjectMapper mapper) {
        properties.validate();
        ClientBuilder builder = Client.builder().endpoints(properties.getEndpoints().toArray(String[]::new));
        if (properties.getUsername() != null && !properties.getUsername().isBlank()) {
            builder.user(ByteSequence.from(properties.getUsername(), StandardCharsets.UTF_8));
            builder.password(ByteSequence.from(properties.getPassword(), StandardCharsets.UTF_8));
        }
        return new EtcdNodeRegistry(builder.build(), mapper, properties);
    }

    @Bean
    ProviderHealthIndicator etcdNodeRegistryHealth(EtcdNodeRegistry registry,
                                                   EtcdNodeRegistryProperties properties) {
        return new ProbeProviderHealthIndicator("node-registry", "etcd", Duration.ofSeconds(5), Duration.ofSeconds(3),
            () -> registry.isAvailable()
                ? ProbeProviderHealthIndicator.up(Map.of("endpoints", properties.getEndpoints()))
                : new PluginHealth(PluginHealth.Status.DOWN, "etcd is unavailable", Map.of()));
    }
}
