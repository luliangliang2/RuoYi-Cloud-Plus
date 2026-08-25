package org.ssssssss.magicapi.iot.plugin.runtime;

import org.ssssssss.magicapi.iot.plugin.api.PluginDescriptor;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.PluginState;

import java.time.Instant;

public record PluginSnapshot(
    PluginDescriptor descriptor,
    PluginState state,
    PluginHealth health,
    Instant updatedAt,
    String lastError
) {
}
