package org.ssssssss.magicapi.iot.handshake.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceAuthenticator;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;

@AutoConfiguration
public class BasicHandshakeAutoConfiguration {
	@Bean
	DeviceHandshakeProvider jsonDeviceHandshakeProvider(ObjectMapper mapper) {
		return new JsonDeviceHandshakeProvider(mapper);
	}

	@Bean
	DeviceAuthenticator registryDeviceAuthenticator(DeviceRegistry registry) {
		return new RegistryDeviceAuthenticator(registry);
	}
}
