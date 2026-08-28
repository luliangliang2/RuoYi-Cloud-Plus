package org.ssssssss.magicapi.iot.script;

import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.CommandGateway;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;

import java.time.Duration;
import java.util.Map;

/** Explicit bindings for script actions; no Spring context or arbitrary bean access is exposed. */
public final class DefaultScriptActionBindings {
    private DefaultScriptActionBindings() { }

    public static void register(ScriptActionExecutorRegistry registry, DeviceMessageBus bus,
                                CommandGateway commands) {
        if (bus != null) registry.register("message.publish", (action, context) -> {
            DeviceIdentity identity = identity(action.parameters());
            DeviceMessageType type = DeviceMessageType.valueOf(String.valueOf(action.parameters().getOrDefault("type", "EVENT_REPORT")));
            bus.publish(new DeviceMessage(null, identity, type, String.valueOf(action.parameters().getOrDefault("protocol", "script")),
                null, null, action.parameters().get("payload"), stringMap(action.parameters().get("metadata"))));
        });
        if (commands != null) registry.register("command.send", (action, context) -> {
            DeviceIdentity identity = identity(action.parameters());
            String operation = String.valueOf(action.parameters().getOrDefault("action", "script"));
            int attempts = Integer.parseInt(String.valueOf(action.parameters().getOrDefault("maxAttempts", 1)));
            commands.submit(new DeviceCommand(String.valueOf(action.parameters().getOrDefault("commandId", "")), identity,
                operation, action.parameters().get("payload"), attempts, Duration.ofSeconds(30), null,
                stringMap(action.parameters().get("metadata"))));
        });
    }

    private static DeviceIdentity identity(Map<String, Object> values) {
        return new DeviceIdentity(String.valueOf(values.get("productId")), String.valueOf(values.get("deviceId")));
    }

    private static Map<String, String> stringMap(Object value) {
        if (!(value instanceof Map<?, ?> map)) return Map.of();
        return map.entrySet().stream().collect(java.util.stream.Collectors.toUnmodifiableMap(
            entry -> String.valueOf(entry.getKey()), entry -> String.valueOf(entry.getValue())));
    }
}
