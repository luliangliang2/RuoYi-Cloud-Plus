package org.ssssssss.magicapi.iot.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.model.DeviceSession;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public class RedisSessionRepository implements SessionRepository {
	private final StringRedisTemplate redis;
	private final ObjectMapper mapper;

	public RedisSessionRepository(StringRedisTemplate redis, ObjectMapper mapper) {
		this.redis = redis;
		this.mapper = mapper;
	}

	private String deviceKey(DeviceIdentity d) {
		return "iot:session:device:" + d.routingKey();
	}

	private String idKey(String id) {
		return "iot:session:id:" + id;
	}

	private String nodeKey(String node) {
		return "iot:session:node:" + node;
	}

	@Override
	public DeviceSession register(DeviceSession s) {
		try {
			DeviceSession old = find(s.device()).orElse(null);
			if (old != null)
				remove(old.sessionId());
			redis.opsForValue().set(deviceKey(s.device()), mapper.writeValueAsString(s));
			redis.opsForValue().set(idKey(s.sessionId()), s.device().routingKey());
			redis.opsForSet().add(nodeKey(s.gatewayNodeId()), s.device().routingKey());
			return s;
		} catch (Exception e) {
			throw new IllegalStateException("Failed to register session: " + s.sessionId(), e);
		}
	}

	@Override
	public Optional<DeviceSession> find(DeviceIdentity d) {
		String v = redis.opsForValue().get(deviceKey(d));
		if (v == null)
			return Optional.empty();
		try {
			return Optional.of(mapper.readValue(v, DeviceSession.class));
		} catch (Exception e) {
			throw new IllegalStateException("Invalid session record", e);
		}
	}

	@Override
	public void touch(String id) {
		String route = redis.opsForValue().get(idKey(id));
		if (route == null)
			return;
		find(new DeviceIdentity(route.substring(0, route.indexOf('/')), route.substring(route.indexOf('/') + 1)))
				.ifPresent(s -> {
					DeviceSession n = new DeviceSession(s.sessionId(), s.device(), s.gatewayNodeId(), s.transport(),
							s.remoteAddress(), s.connectedAt(), Instant.now(), s.attributes());
					try {
						redis.opsForValue().set(deviceKey(s.device()), mapper.writeValueAsString(n));
					} catch (Exception e) {
						throw new IllegalStateException(e);
					}
				});
	}

	@Override
	public void remove(String id) {
		String route = redis.opsForValue().get(idKey(id));
		if (route == null)
			return;
		redis.delete(idKey(id));
		String[] p = route.split("/", 2);
		find(new DeviceIdentity(p[0], p[1])).ifPresent(s -> {
			redis.delete(deviceKey(s.device()));
			redis.opsForSet().remove(nodeKey(s.gatewayNodeId()), route);
		});
	}

	@Override
	public Collection<DeviceSession> findByGatewayNode(String node) {
		return redis.opsForSet().members(nodeKey(node)).stream().map(r -> {
			String[] p = r.split("/", 2);
			return find(new DeviceIdentity(p[0], p[1])).orElse(null);
		}).filter(java.util.Objects::nonNull).toList();
	}
}
