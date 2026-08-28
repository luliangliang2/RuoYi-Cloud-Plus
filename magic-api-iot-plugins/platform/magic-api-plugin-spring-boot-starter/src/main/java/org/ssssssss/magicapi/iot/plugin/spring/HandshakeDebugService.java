package org.ssssssss.magicapi.iot.plugin.spring;

import org.ssssssss.magicapi.iot.core.spi.DeviceAuthenticator;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRuntimeException;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginServiceRegistry;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;

public final class HandshakeDebugService {
    private final PluginServiceRegistry services;

    HandshakeDebugService(PluginServiceRegistry services) {
        this.services = services;
    }

    public DebugResult debug(DebugRequest request) {
        byte[] payload = decode(request.payload(), request.encoding());
        var context = new DeviceHandshakeProvider.HandshakeContext("dry-run", request.protocolId(),
            request.transportId(), request.remoteAddress(), Instant.now(), 1,
            Map.of("dryRun", "true"));
        var handshake = services.invoke(DeviceHandshakeProvider.class, request.handshakeProviderId(),
            provider -> {
                if (!provider.supportedProtocols().contains(request.protocolId()))
                    throw new PluginRuntimeException("Handshake provider does not support protocol: " + request.protocolId());
                return provider.onMessage(context, ByteBuffer.wrap(payload));
            });
        DeviceAuthenticator.AuthenticationResult authentication = null;
        if (handshake.status() == DeviceHandshakeProvider.Status.AUTHENTICATE) {
            if (handshake.identity() == null || handshake.credential() == null || handshake.authenticatorId().isBlank())
                throw new PluginRuntimeException("Handshake AUTHENTICATE result is incomplete");
            authentication = services.invoke(DeviceAuthenticator.class, handshake.authenticatorId(),
                authenticator -> authenticator.authenticate(handshake.identity(), handshake.credential(),
                    new DeviceAuthenticator.AuthenticationContext(request.protocolId(), request.remoteAddress(), "",
                        Instant.now(), Map.of("dryRun", "true"))));
        }
        return new DebugResult(true, request.handshakeProviderId(), handshake.authenticatorId(), handshake.status().name(),
            handshake.identity(), authentication, Base64.getEncoder().encodeToString(handshake.response()),
            handshake.reason(), handshake.attributes());
    }

    private static byte[] decode(String payload, String encoding) {
        String value = payload == null ? "" : payload;
        return switch (encoding == null ? "text" : encoding.toLowerCase()) {
            case "base64" -> Base64.getDecoder().decode(value);
            case "hex" -> HexFormat.of().parseHex(value.replaceAll("\\s+", ""));
            case "text" -> value.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            default -> throw new IllegalArgumentException("Unsupported payload encoding: " + encoding);
        };
    }

    public record DebugRequest(String protocolId, String transportId, String remoteAddress,
                               String handshakeProviderId, String encoding, String payload) {
        public DebugRequest {
            transportId = transportId == null || transportId.isBlank() ? "debug" : transportId;
            remoteAddress = remoteAddress == null || remoteAddress.isBlank() ? "127.0.0.1:0" : remoteAddress;
            encoding = encoding == null || encoding.isBlank() ? "text" : encoding;
        }
    }

    public record DebugResult(boolean dryRun, String handshakeProviderId, String authenticatorId,
                              String handshakeStatus, org.ssssssss.magicapi.iot.core.model.DeviceIdentity identity,
                              DeviceAuthenticator.AuthenticationResult authentication, String responseBase64,
                              String reason, Map<String, String> attributes) { }
}
