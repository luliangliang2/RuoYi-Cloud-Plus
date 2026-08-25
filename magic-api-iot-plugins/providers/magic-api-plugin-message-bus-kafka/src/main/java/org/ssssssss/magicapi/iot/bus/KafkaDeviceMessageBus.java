package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import java.util.Objects;
import java.util.function.Consumer;

public class KafkaDeviceMessageBus implements DeviceMessageBus {
	private final KafkaTemplate<String, String> kafka;
	private final ObjectMapper mapper;
	private final String topic;

	public KafkaDeviceMessageBus(KafkaTemplate<String, String> kafka, ObjectMapper mapper, String topic) {
		this.kafka = kafka;
		this.mapper = mapper;
		this.topic = topic;
	}

	@Override
	public void publish(DeviceMessage message) {
		try {
			kafka.send(topic, message.device().routingKey(), mapper.writeValueAsString(message));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to publish device message", e);
		}
	}

	@Override
	public Subscription subscribe(String id, Consumer<DeviceMessage> consumer) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(consumer);
		throw new UnsupportedOperationException("Kafka subscription is configured by the consumer container");
	}
}
