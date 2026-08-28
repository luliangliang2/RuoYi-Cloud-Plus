package org.ssssssss.magicapi.iot.plugin.spring;

import org.ssssssss.magicapi.iot.core.model.DeviceSession;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.DeviceAuthenticator;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;
import org.ssssssss.magicapi.iot.core.spi.HandshakeCoordinator;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRuntimeException;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginServiceRegistry;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class DefaultHandshakeCoordinator implements HandshakeCoordinator {
    private final HandshakeProperties properties;
    private final PluginServiceRegistry services;
    private final SessionRepository sessions;
    private final Map<String, State> states = new ConcurrentHashMap<>();

    DefaultHandshakeCoordinator(HandshakeProperties properties, PluginServiceRegistry services,
                                SessionRepository sessions) {
        this.properties = properties;
        this.services = services;
        this.sessions = sessions;
    }

    @Override
    public void connected(String connectionId, ProtocolContext context) {
        String protocolId = protocolId(context);
        if (!properties.isEnabled() || !properties.getRequiredProtocols().contains(protocolId)) return;
        String providerId = properties.getProviders().get(protocolId);
        if (providerId == null || providerId.isBlank()) {
            states.put(connectionId, new State(context, "", Instant.now()));
            states.get(connectionId).rejected = "Required handshake provider is not configured for protocol: " + protocolId;
            return;
        }
        State state = new State(context, providerId, Instant.now());
        states.put(connectionId, state);
        var result = services.invoke(DeviceHandshakeProvider.class, providerId,
            provider -> provider.onConnect(handshakeContext(connectionId, state, protocolId)));
        if (result.status() == DeviceHandshakeProvider.Status.REJECTED || result.status() == DeviceHandshakeProvider.Status.CLOSE)
            state.rejected = result.reason();
    }

    @Override
    public Decision received(String connectionId, ByteBuffer payload, ProtocolContext context) {
        State state = states.get(connectionId);
        if (state == null || state.authenticatedContext != null)
            return Decision.forward(state == null ? context : state.authenticatedContext);
        if (!state.rejected.isBlank()) return new Decision(false, true, null, context, state.rejected);
        if (state.providerId.isBlank()) return Decision.reject("Handshake provider is not configured");
        if (Instant.now().isAfter(state.connectedAt.plus(properties.getTimeout())))
            return new Decision(false, true, null, context, "Handshake timed out");
        if (++state.attempts > properties.getMaxAttempts())
            return new Decision(false, true, null, context, "Handshake attempts exceeded");

        String protocolId = protocolId(context);
        var result = services.invoke(DeviceHandshakeProvider.class, state.providerId,
            provider -> provider.onMessage(handshakeContext(connectionId, state, protocolId), payload.asReadOnlyBuffer()));
        ByteBuffer response = ByteBuffer.wrap(result.response());
        if (result.status() == DeviceHandshakeProvider.Status.AUTHENTICATE) {
            var authentication = services.invoke(DeviceAuthenticator.class, result.authenticatorId(), authenticator ->
                authenticator.authenticate(result.identity(), result.credential(),
                    new DeviceAuthenticator.AuthenticationContext(protocolId, context.remoteAddress(), "", Instant.now(),
                        Map.of("connectionId", connectionId))));
            if (!authentication.authenticated())
                return new Decision(false, true, response, context, authentication.reason());
            Map<String, String> attributes = new HashMap<>(context.attributes());
            attributes.put("authenticated", "true");
            attributes.put("principal", authentication.principal());
            attributes.put("handshakeProvider", state.providerId);
            ProtocolContext authenticated = new ProtocolContext(context.transport(), context.remoteAddress(),
                result.identity(), attributes);
            state.authenticatedContext = authenticated;
            sessions.register(new DeviceSession(connectionId, result.identity(), properties.getGatewayNodeId(),
                context.transport(), context.remoteAddress(), state.connectedAt, Instant.now(), attributes));
            return new Decision(false, false, response, authenticated, "Authenticated");
        }
        boolean close = result.status() == DeviceHandshakeProvider.Status.REJECTED
            || result.status() == DeviceHandshakeProvider.Status.CLOSE;
        return new Decision(false, close, response, context, result.reason());
    }

    @Override public void disconnected(String connectionId) {
        State state = states.remove(connectionId);
        if (state != null && state.authenticatedContext != null) sessions.remove(connectionId);
    }

    private DeviceHandshakeProvider.HandshakeContext handshakeContext(String connectionId, State state,
                                                                       String protocolId) {
        return new DeviceHandshakeProvider.HandshakeContext(connectionId, protocolId,
            state.originalContext.transport(), state.originalContext.remoteAddress(), state.connectedAt,
            state.attempts, state.originalContext.attributes());
    }

    private static String protocolId(ProtocolContext context) {
        return context.attributes().getOrDefault("protocolId", context.device().productId());
    }

    private static final class State {
        private final ProtocolContext originalContext;
        private final String providerId;
        private final Instant connectedAt;
        private int attempts;
        private volatile ProtocolContext authenticatedContext;
        private volatile String rejected = "";

        private State(ProtocolContext originalContext, String providerId, Instant connectedAt) {
            this.originalContext = originalContext;
            this.providerId = providerId;
            this.connectedAt = connectedAt;
        }
    }
}
