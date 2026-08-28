package org.ssssssss.magicapi.iot.core.session;

import org.ssssssss.magicapi.iot.core.model.DeviceSession;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/** Shared session lifecycle boundary for transports and broker lifecycle adapters. */
public final class SessionLifecycleCoordinator {
    private final SessionRepository repository;
    private final String gatewayNodeId;

    public SessionLifecycleCoordinator(SessionRepository repository, String gatewayNodeId) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.gatewayNodeId = gatewayNodeId == null || gatewayNodeId.isBlank() ? "unknown" : gatewayNodeId;
    }

    public DeviceSession connected(String sessionId, ProtocolContext context) {
        Objects.requireNonNull(sessionId, "sessionId");
        Objects.requireNonNull(context, "context");
        return repository.register(new DeviceSession(sessionId, context.device(), gatewayNodeId,
            context.transport(), context.remoteAddress(), Instant.now(), Instant.now(), context.attributes()));
    }

    public void connected(String sessionId, ProtocolContext context, Map<String, String> attributes) {
        Map<String, String> merged = new java.util.LinkedHashMap<>(context.attributes());
        if (attributes != null) merged.putAll(attributes);
        connected(sessionId, new ProtocolContext(context.transport(), context.remoteAddress(), context.device(), merged));
    }

    public void touch(String sessionId) { repository.touch(sessionId); }

    public void disconnected(String sessionId) {
        if (sessionId != null && !sessionId.isBlank()) repository.remove(sessionId);
    }
}
