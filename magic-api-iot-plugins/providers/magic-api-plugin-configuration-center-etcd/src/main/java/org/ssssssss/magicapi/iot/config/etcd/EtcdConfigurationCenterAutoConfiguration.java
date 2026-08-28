package org.ssssssss.magicapi.iot.config.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(EtcdConfigurationCenterProperties.class)
@ConditionalOnProperty(prefix = "iot.providers.configuration-center", name = "type", havingValue = "etcd")
public class EtcdConfigurationCenterAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(ConfigurationCenter.class)
    EtcdConfigurationCenter etcdConfigurationCenter(EtcdConfigurationCenterProperties properties) {
        properties.validate();
        ClientBuilder builder = Client.builder().endpoints(properties.getEndpoints().toArray(String[]::new));
        if (properties.getUsername() != null && !properties.getUsername().isBlank()) {
            builder.user(ByteSequence.from(properties.getUsername(), StandardCharsets.UTF_8));
            builder.password(ByteSequence.from(properties.getPassword(), StandardCharsets.UTF_8));
        }
        return new EtcdConfigurationCenter(builder.build(), properties);
    }

    @Bean
    ProviderHealthIndicator etcdConfigurationCenterHealth(EtcdConfigurationCenter center,
                                                           EtcdConfigurationCenterProperties properties) {
        return new ProbeProviderHealthIndicator("configuration-center", "etcd", Duration.ofSeconds(5), Duration.ofSeconds(3),
            () -> center.isAvailable()
                ? ProbeProviderHealthIndicator.up(Map.of("endpoints", properties.getEndpoints(), "rootPrefix", properties.getRootPrefix()))
                : new PluginHealth(PluginHealth.Status.DOWN, "etcd configuration center is unavailable", Map.of()));
    }
}
