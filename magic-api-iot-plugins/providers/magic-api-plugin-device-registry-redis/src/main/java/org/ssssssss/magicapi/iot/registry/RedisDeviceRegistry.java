package org.ssssssss.magicapi.iot.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;

public class RedisDeviceRegistry implements DeviceRegistry {
	private final StringRedisTemplate redis;
	private final ObjectMapper mapper;

	public RedisDeviceRegistry(StringRedisTemplate redis, ObjectMapper mapper) {
		this.redis = redis;
		this.mapper = mapper;
	}

	private String key(DeviceIdentity id) {
		return "iot:device:" + id.routingKey();
	}

	private String credentialKey(DeviceIdentity id, DeviceCredential c) {
		return key(id) + ":credential:" + c.type();
	}

	@Override
	public Optional<RegisteredDevice> find(DeviceIdentity identity) {
		String value = redis.opsForValue().get(key(identity));
		if (value == null)
			return Optional.empty();
		try {
			return Optional.of(mapper.readValue(value, RegisteredDevice.class));
		} catch (Exception e) {
			throw new IllegalStateException("Invalid device registry record: " + identity.routingKey(), e);
		}
	}

	@Override
	public RegisteredDevice save(RegisteredDevice device) {
		try {
			redis.opsForValue().set(key(device.identity()), mapper.writeValueAsString(device));
			return device;
		} catch (Exception e) {
			throw new IllegalStateException("Failed to save device: " + device.identity().routingKey(), e);
		}
	}

	public void setCredential(DeviceIdentity identity, DeviceCredential credential) {
		redis.opsForValue().set(credentialKey(identity, credential), hash(credential.value()));
	}

	@Override
	public boolean authenticate(DeviceIdentity identity, DeviceCredential credential) {
		return find(identity).filter(RegisteredDevice::enabled).isPresent()
				&& MessageDigest.isEqual(value(credentialKey(identity, credential)).getBytes(StandardCharsets.UTF_8),
						hash(credential.value()).getBytes(StandardCharsets.UTF_8));
	}

	private String value(String key) {
		String v = redis.opsForValue().get(key);
		return v == null ? "" : v;
	}

	private static String hash(String value) {
		try {
			return HexFormat.of()
					.formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
