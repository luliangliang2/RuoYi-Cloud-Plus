package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.ssssssss.magicapi.iot.plugin.api.PluginService;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginServiceRegistry;

import java.util.List;

final class ClasspathPluginServiceRegistrar implements SmartInitializingSingleton {
    private final PluginServiceRegistry registry;
    private final List<PluginService> services;

    ClasspathPluginServiceRegistrar(PluginServiceRegistry registry, List<PluginService> services) {
        this.registry = registry;
        this.services = List.copyOf(services);
    }

    @Override public void afterSingletonsInstantiated() {
        services.forEach(service -> registry.register(service.ownerPluginId(), service, "classpath"));
    }
}
