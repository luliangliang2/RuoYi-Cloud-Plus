package org.ssssssss.magicapi.iot.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistryAdmin;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;

public class RedisDeviceRegistry implements DeviceRegistry, DeviceRegistryAdmin {
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

	private String allDevicesIndex() { return "iot:device:index:all"; }

	private String productIndex(String productId) { return "iot:device:index:product:" + productId; }

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
			String oldProduct = find(device.identity()).map(existing -> existing.identity().productId()).orElse(null);
			redis.opsForValue().set(key(device.identity()), mapper.writeValueAsString(device));
			redis.opsForSet().add(allDevicesIndex(), device.identity().routingKey());
			if (oldProduct != null && !oldProduct.equals(device.identity().productId()))
				redis.opsForSet().remove(productIndex(oldProduct), device.identity().routingKey());
			redis.opsForSet().add(productIndex(device.identity().productId()), device.identity().routingKey());
			return device;
		} catch (Exception e) {
			throw new IllegalStateException("Failed to save device: " + device.identity().routingKey(), e);
		}
	}

	@Override
	public RegisteredDevice register(RegisteredDevice device) {
		if (find(device.identity()).isPresent())
			throw new IllegalArgumentException("Device is already registered: " + device.identity().routingKey());
		return save(device);
	}

	@Override
	public RegisteredDevice update(RegisteredDevice device) {
		if (find(device.identity()).isEmpty())
			throw new IllegalArgumentException("Device is not registered: " + device.identity().routingKey());
		return save(device);
	}

	@Override
	public void delete(DeviceIdentity identity) {
		if (find(identity).isEmpty())
			throw new IllegalArgumentException("Device is not registered: " + identity.routingKey());
		redis.delete(key(identity));
		redis.opsForSet().remove(allDevicesIndex(), identity.routingKey());
		redis.opsForSet().remove(productIndex(identity.productId()), identity.routingKey());
		String prefix = key(identity) + ":credential:";
		redis.execute((org.springframework.data.redis.core.RedisCallback<Void>) connection -> {
			try (var cursor = connection.scan(org.springframework.data.redis.core.ScanOptions.scanOptions().match(prefix + "*").count(100).build())) {
				while (cursor.hasNext()) connection.keyCommands().del(cursor.next());
			}
			return null;
		});
	}

	@Override
	public void setCredential(DeviceIdentity identity, DeviceCredential credential) {
		redis.opsForValue().set(credentialKey(identity, credential), hash(credential.value()));
	}

	@Override
	public DevicePage search(String productId, String keyword, int page, int pageSize) {
		int safePage = Math.max(1, page);
		int safeSize = Math.min(100, Math.max(1, pageSize));
		String product = productId == null ? "" : productId.trim();
		String query = keyword == null ? "" : keyword.trim().toLowerCase();
		java.util.List<RegisteredDevice> matched = new java.util.ArrayList<>();
		java.util.Set<String> identities = product.isEmpty()
				? redis.opsForSet().members(allDevicesIndex())
				: redis.opsForSet().members(productIndex(product));
		if (identities == null) identities = java.util.Set.of();
		for (String routingKey : identities) {
			String[] parts = routingKey.split("/", 2);
			if (parts.length != 2 || (!query.isEmpty() && !parts[1].toLowerCase().contains(query))) continue;
			String value = redis.opsForValue().get(key(new DeviceIdentity(parts[0], parts[1])));
			if (value == null) continue;
			try { matched.add(mapper.readValue(value, RegisteredDevice.class)); }
			catch (Exception exception) { throw new IllegalStateException("Invalid device registry record: " + routingKey, exception); }
		}
		matched.sort(java.util.Comparator.comparing(device -> device.identity().routingKey()));
		int from = Math.min(matched.size(), (safePage - 1) * safeSize);
		int to = Math.min(matched.size(), from + safeSize);
		return new DevicePage(matched.subList(from, to), matched.size(), safePage, safeSize);
	}

	@Override
	public RegisteredDevice setEnabled(DeviceIdentity identity, boolean enabled) {
		RegisteredDevice current = find(identity).orElseThrow(() -> new IllegalArgumentException("Device is not registered: " + identity.routingKey()));
		return save(new RegisteredDevice(identity, enabled, current.protocolId(), current.capabilities(), current.version() + 1));
	}

	@Override
	public java.util.Set<String> credentialTypes(DeviceIdentity identity) {
		java.util.Set<String> types = new java.util.HashSet<>();
		String prefix = key(identity) + ":credential:";
		redis.execute((org.springframework.data.redis.core.RedisCallback<Void>) connection -> {
			try (var cursor = connection.scan(org.springframework.data.redis.core.ScanOptions.scanOptions()
					.match(prefix + "*").count(100).build())) {
				while (cursor.hasNext()) {
					String redisKey = new String(cursor.next(), StandardCharsets.UTF_8);
					types.add(redisKey.substring(prefix.length()));
				}
			}
			return null;
		});
		return java.util.Set.copyOf(types);
	}

	@Override
	public void deleteCredential(DeviceIdentity identity, String credentialType) {
		redis.delete(key(identity) + ":credential:" + credentialType);
	}

	@Override
	public boolean verifyCredential(DeviceIdentity identity, DeviceCredential credential) {
		return authenticate(identity, credential);
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
