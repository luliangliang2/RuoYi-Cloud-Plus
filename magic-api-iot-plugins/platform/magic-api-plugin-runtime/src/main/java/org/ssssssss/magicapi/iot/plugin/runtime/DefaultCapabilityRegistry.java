package org.ssssssss.magicapi.iot.plugin.runtime;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultCapabilityRegistry implements CapabilityRegistry {

    private final ConcurrentMap<String, CopyOnWriteArrayList<String>> providers = new ConcurrentHashMap<>();

    @Override
    public void register(String pluginId, List<String> capabilities) {
        capabilities.forEach(capability -> providers
            .computeIfAbsent(capability, ignored -> new CopyOnWriteArrayList<>())
            .addIfAbsent(pluginId));
    }

    @Override
    public List<String> providers(String capability) {
        return List.copyOf(providers.getOrDefault(capability, new CopyOnWriteArrayList<>()));
    }

    @Override
    public Map<String, List<String>> capabilities() {
        return providers.entrySet().stream().collect(java.util.stream.Collectors.toUnmodifiableMap(
            Map.Entry::getKey, entry -> List.copyOf(entry.getValue())));
    }
}
