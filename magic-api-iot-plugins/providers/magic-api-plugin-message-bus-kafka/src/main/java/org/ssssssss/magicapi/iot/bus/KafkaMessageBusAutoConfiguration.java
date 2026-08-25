package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ConsumerFactory;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.message-bus", name = "type", havingValue = "kafka")
@ConditionalOnBean(KafkaTemplate.class)
public class KafkaMessageBusAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean(DeviceMessageBus.class)
	DeviceMessageBus deviceMessageBus(KafkaTemplate<String, String> kafka, ConsumerFactory<String, String> consumerFactory, ObjectMapper mapper,
			org.springframework.core.env.Environment env) {
		return new KafkaDeviceMessageBus(kafka, consumerFactory, mapper,
				env.getProperty("iot.providers.message-bus.topic", "iot-device-messages"),
				env.getProperty("iot.providers.message-bus.retries", Integer.class, 3),
				env.getProperty("iot.providers.message-bus.retry-delay", java.time.Duration.class, java.time.Duration.ofSeconds(1)),
				env.getProperty("iot.providers.message-bus.max-pending", Integer.class, 1000));
	}
}
