package org.ssssssss.magicapi.iot.plugin.runtime;

import org.ssssssss.magicapi.iot.plugin.api.PluginState;

import java.time.Instant;
import java.util.List;

public record ExternalPluginSnapshot(
    String pluginId,
    String version,
    String jar,
    String classLoader,
    PluginState state,
    List<String> services,
    Instant loadedAt,
    String lastError
) {
    public ExternalPluginSnapshot {
        services = services == null ? List.of() : List.copyOf(services);
        lastError = lastError == null ? "" : lastError;
    }
}
