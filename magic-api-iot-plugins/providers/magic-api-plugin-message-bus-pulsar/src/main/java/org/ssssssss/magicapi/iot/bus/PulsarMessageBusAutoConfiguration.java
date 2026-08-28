package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.message-bus", name = "type", havingValue = "pulsar")
public class PulsarMessageBusAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    PulsarClient pulsarClient(org.springframework.core.env.Environment environment) throws Exception {
        return PulsarClient.builder().serviceUrl(environment.getProperty(
            "iot.providers.message-bus.service-url", "pulsar://127.0.0.1:6650")).build();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(DeviceMessageBus.class)
    DeviceMessageBus deviceMessageBus(PulsarClient client, ObjectMapper mapper,
                                      org.springframework.core.env.Environment environment) throws Exception {
        return new PulsarDeviceMessageBus(client, mapper,
            environment.getProperty("iot.providers.message-bus.topic", "iot-device-messages"),
            environment.getProperty("iot.providers.message-bus.retries", Integer.class, 3),
            environment.getProperty("iot.providers.message-bus.retry-delay-ms", Long.class, 1000L),
            environment.getProperty("iot.providers.message-bus.max-pending", Integer.class, 1000));
    }

    @Bean
    ProviderHealthIndicator pulsarMessageBusHealth(PulsarClient client,
                                                   org.springframework.core.env.Environment environment) {
        String topic = environment.getProperty("iot.providers.message-bus.topic", "iot-device-messages");
        Duration timeout = timeout(environment);
        return new ProbeProviderHealthIndicator("message-bus", "pulsar", cacheTtl(environment), timeout,
            () -> {
                var partitions = client.getPartitionsForTopic(topic).get(timeout.toMillis(), TimeUnit.MILLISECONDS);
                return ProbeProviderHealthIndicator.up(Map.of(
                    "topic", topic, "partitions", partitions.size(), "clientClosed", client.isClosed()));
            });
    }

    private static Duration cacheTtl(org.springframework.core.env.Environment environment) {
        return environment.getProperty("iot.health.cache-ttl", Duration.class, Duration.ofSeconds(10));
    }

    private static Duration timeout(org.springframework.core.env.Environment environment) {
        return environment.getProperty("iot.health.timeout", Duration.class, Duration.ofSeconds(3));
    }
}
