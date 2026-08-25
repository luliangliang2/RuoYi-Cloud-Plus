package org.ssssssss.magicapi.iot.session;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.model.DeviceSession;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionRepository implements SessionRepository {

    private final ConcurrentHashMap<String, DeviceSession> byDevice = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> sessionToDevice = new ConcurrentHashMap<>();

    @Override
    public DeviceSession register(DeviceSession session) {
        DeviceSession previous = byDevice.put(session.device().routingKey(), session);
        if (previous != null) sessionToDevice.remove(previous.sessionId());
        sessionToDevice.put(session.sessionId(), session.device().routingKey());
        return session;
    }

    @Override
    public Optional<DeviceSession> find(DeviceIdentity device) {
        return Optional.ofNullable(byDevice.get(device.routingKey()));
    }

    @Override
    public void touch(String sessionId) {
        String key = sessionToDevice.get(sessionId);
        if (key != null) {
            byDevice.computeIfPresent(key, (ignored, current) -> new DeviceSession(
                current.sessionId(), current.device(), current.gatewayNodeId(), current.transport(),
                current.remoteAddress(), current.connectedAt(), Instant.now(), current.attributes()));
        }
    }

    @Override
    public void remove(String sessionId) {
        String key = sessionToDevice.remove(sessionId);
        if (key != null) byDevice.computeIfPresent(key, (ignored, value) -> value.sessionId().equals(sessionId) ? null : value);
    }

    @Override
    public Collection<DeviceSession> findByGatewayNode(String gatewayNodeId) {
        return byDevice.values().stream().filter(it -> it.gatewayNodeId().equals(gatewayNodeId)).toList();
    }
}

