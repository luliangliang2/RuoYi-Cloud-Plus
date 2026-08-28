package org.ssssssss.magicapi.iot.script;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceAuthenticator;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;
import org.ssssssss.magicapi.iot.core.spi.DeviceRouteRepository;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginServiceRegistry;

import java.time.Instant;
import java.util.Map;

/** Binds script actions to registered SPI services only. It never exposes the application context. */
public final class PluginServiceActionBindings {
    private PluginServiceActionBindings() { }

    public static void register(ScriptActionExecutorRegistry target, PluginServiceRegistry services,
                                DeviceRouteRepository routes) {
        if (services == null) throw new IllegalArgumentException("services must not be null");
        target.register("handshake.connect", (action, context) -> services.invoke(DeviceHandshakeProvider.class,
            required(action, "providerId"), provider -> provider.onConnect(handshakeContext(action, context))));
        target.register("handshake.message", (action, context) -> services.invoke(DeviceHandshakeProvider.class,
            required(action, "providerId"), provider -> provider.onMessage(handshakeContext(action, context),
                java.nio.ByteBuffer.wrap(java.util.Base64.getDecoder().decode(required(action, "payloadBase64"))))));
        target.register("device.authenticate", (action, context) -> services.invoke(DeviceAuthenticator.class,
            required(action, "providerId"), provider -> provider.authenticate(identity(action),
                new DeviceCredential(required(action, "credentialType"), required(action, "credentialValue")),
                new DeviceAuthenticator.AuthenticationContext(context.trigger(), value(action, "remoteAddress", ""),
                    value(action, "nonce", ""), Instant.now(), Map.of("traceId", context.traceId())))));
        if (routes != null) {
            target.register("route.bind", (action, context) -> routes.upsert(new DeviceRouteRepository.DeviceRoute(
                identity(action), required(action, "nodeId"), required(action, "channelId"), Instant.now(),
                longValue(action, "version", 0))));
            target.register("route.unbind", (action, context) -> routes.remove(identity(action),
                required(action, "expectedSessionId")));
        }
    }

    private static DeviceHandshakeProvider.HandshakeContext handshakeContext(ScriptExecutionResult.ScriptAction action,
                                                                               ScriptExecutionContext context) {
        return new DeviceHandshakeProvider.HandshakeContext(value(action, "connectionId", context.traceId()),
            value(action, "protocolId", "script"), value(action, "transportId", "script"),
            value(action, "remoteAddress", ""), Instant.now(), (int) longValue(action, "attempt", 1), Map.of());
    }

    private static DeviceIdentity identity(ScriptExecutionResult.ScriptAction action) {
        return new DeviceIdentity(required(action, "productId"), required(action, "deviceId"));
    }

    private static String required(ScriptExecutionResult.ScriptAction action, String name) {
        String value = value(action, name, "");
        if (value.isBlank()) throw new IllegalArgumentException("Missing action parameter: " + name);
        return value;
    }

    private static String value(ScriptExecutionResult.ScriptAction action, String name, String fallback) {
        Object value = action.parameters().get(name);
        return value == null ? fallback : String.valueOf(value);
    }

    private static long longValue(ScriptExecutionResult.ScriptAction action, String name, long fallback) {
        try { return Long.parseLong(value(action, name, String.valueOf(fallback))); }
        catch (NumberFormatException exception) { throw new IllegalArgumentException("Invalid action parameter: " + name); }
    }
}
