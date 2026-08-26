package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.message-bus", name = "type", havingValue = "rocketmq")
@ConditionalOnBean(RocketMQTemplate.class)
public class RocketMqMessageBusAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(DeviceMessageBus.class)
    DeviceMessageBus deviceMessageBus(RocketMQTemplate template, ObjectMapper mapper,
                                      org.springframework.core.env.Environment environment) {
        return new RocketMqDeviceMessageBus(template, mapper, nameServer(environment),
            environment.getProperty("iot.providers.message-bus.topic", "iot-device-messages"),
            environment.getProperty("iot.providers.message-bus.retries", Integer.class, 3));
    }

    @Bean
    ProviderHealthIndicator rocketMqMessageBusHealth(RocketMQTemplate template,
                                                     org.springframework.core.env.Environment environment) {
        String topic = environment.getProperty("iot.providers.message-bus.topic", "iot-device-messages");
        return new ProbeProviderHealthIndicator("message-bus", "rocketmq", cacheTtl(environment),
            timeout(environment), () -> {
                var queues = template.getProducer().fetchPublishMessageQueues(topic);
                return Map.of("topic", topic, "queues", queues.size(), "nameServer", nameServer(environment));
            });
    }

    private static String nameServer(org.springframework.core.env.Environment environment) {
        return environment.getProperty("iot.providers.message-bus.name-server",
            environment.getProperty("rocketmq.name-server", "127.0.0.1:9876"));
    }

    private static Duration cacheTtl(org.springframework.core.env.Environment environment) {
        return environment.getProperty("iot.health.cache-ttl", Duration.class, Duration.ofSeconds(10));
    }

    private static Duration timeout(org.springframework.core.env.Environment environment) {
        return environment.getProperty("iot.health.timeout", Duration.class, Duration.ofSeconds(3));
    }
}
