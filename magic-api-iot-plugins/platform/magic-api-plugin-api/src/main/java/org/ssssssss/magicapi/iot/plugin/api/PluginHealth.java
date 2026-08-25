package org.ssssssss.magicapi.iot.plugin.api;

import java.util.Map;

public record PluginHealth(Status status, String message, Map<String, Object> details) {

    public PluginHealth {
        details = details == null ? Map.of() : Map.copyOf(details);
    }

    public static PluginHealth up() {
        return new PluginHealth(Status.UP, "", Map.of());
    }

    public static PluginHealth down(String message) {
        return new PluginHealth(Status.DOWN, message, Map.of());
    }

    public enum Status {
        UP,
        DEGRADED,
        DOWN,
        UNKNOWN
    }
}
