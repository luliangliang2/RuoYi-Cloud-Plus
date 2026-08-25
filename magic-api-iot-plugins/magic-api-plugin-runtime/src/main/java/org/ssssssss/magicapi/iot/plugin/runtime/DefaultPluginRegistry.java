package org.ssssssss.magicapi.iot.plugin.runtime;

import org.ssssssss.magicapi.iot.plugin.api.PluginDescriptor;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.PluginState;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultPluginRegistry implements PluginRegistry {

    private final ConcurrentMap<String, PluginSnapshot> plugins = new ConcurrentHashMap<>();

    @Override
    public void register(PluginDescriptor descriptor) {
        validateDescriptor(descriptor);
        PluginSnapshot snapshot = new PluginSnapshot(
            descriptor, PluginState.REGISTERED,
            new PluginHealth(PluginHealth.Status.UNKNOWN, "Plugin registered", null),
            Instant.now(), "");
        if (plugins.putIfAbsent(descriptor.id(), snapshot) != null) {
            throw new PluginRuntimeException("Duplicate plugin id: " + descriptor.id());
        }
    }

    @Override
    public void update(String pluginId, PluginState state, PluginHealth health, String lastError) {
        plugins.compute(pluginId, (id, current) -> {
            if (current == null) {
                throw new PluginRuntimeException("Plugin is not registered: " + pluginId);
            }
            return new PluginSnapshot(current.descriptor(), state, health, Instant.now(),
                lastError == null ? "" : lastError);
        });
    }

    @Override
    public Optional<PluginSnapshot> find(String pluginId) {
        return Optional.ofNullable(plugins.get(pluginId));
    }

    @Override
    public List<PluginSnapshot> snapshots() {
        return plugins.values().stream()
            .sorted(Comparator.comparingInt((PluginSnapshot item) -> item.descriptor().loadOrder())
                .thenComparing(item -> item.descriptor().id()))
            .toList();
    }

    private void validateDescriptor(PluginDescriptor descriptor) {
        if (descriptor == null || descriptor.id() == null || descriptor.id().isBlank()) {
            throw new PluginRuntimeException("Plugin id must not be blank");
        }
        if (descriptor.version() == null || descriptor.version().isBlank()) {
            throw new PluginRuntimeException("Plugin version must not be blank: " + descriptor.id());
        }
        if (descriptor.apiVersion() == null || descriptor.apiVersion().isBlank()) {
            throw new PluginRuntimeException("Plugin API version must not be blank: " + descriptor.id());
        }
    }
}
