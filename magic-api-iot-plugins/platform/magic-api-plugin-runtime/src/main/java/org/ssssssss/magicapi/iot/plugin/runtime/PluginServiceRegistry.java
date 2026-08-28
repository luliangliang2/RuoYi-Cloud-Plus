package org.ssssssss.magicapi.iot.plugin.runtime;

import org.ssssssss.magicapi.iot.plugin.api.PluginService;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface PluginServiceRegistry {

    void register(String pluginId, PluginService service, String source);

    void unregisterPlugin(String pluginId);

    <T extends PluginService> Optional<T> find(Class<T> serviceType, String serviceId);

    <T extends PluginService> List<T> services(Class<T> serviceType);

    <T extends PluginService, R> R invoke(Class<T> serviceType, String serviceId, Function<T, R> invocation);

    List<PluginServiceSnapshot> snapshots();
}
