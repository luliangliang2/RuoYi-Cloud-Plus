package org.ssssssss.magicapi.iot.plugin.runtime;

import org.ssssssss.magicapi.iot.plugin.api.PluginDescriptor;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.PluginState;

import java.util.List;
import java.util.Optional;

public interface PluginRegistry {

    void register(PluginDescriptor descriptor);

    void update(String pluginId, PluginState state, PluginHealth health, String lastError);

    void unregister(String pluginId);

    Optional<PluginSnapshot> find(String pluginId);

    List<PluginSnapshot> snapshots();
}
