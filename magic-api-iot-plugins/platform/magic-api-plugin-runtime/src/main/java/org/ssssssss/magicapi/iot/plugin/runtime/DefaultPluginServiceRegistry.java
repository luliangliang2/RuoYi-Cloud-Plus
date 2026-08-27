package org.ssssssss.magicapi.iot.plugin.runtime;

import org.ssssssss.magicapi.iot.plugin.api.PluginService;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public final class DefaultPluginServiceRegistry implements PluginServiceRegistry {
    private final ConcurrentMap<ServiceKey, Entry> services = new ConcurrentHashMap<>();

    @Override
    public void register(String pluginId, PluginService service, String source) {
        if (pluginId == null || pluginId.isBlank()) throw new PluginRuntimeException("Plugin id must not be blank");
        if (service == null || service.serviceId() == null || service.serviceId().isBlank())
            throw new PluginRuntimeException("Plugin service id must not be blank: " + pluginId);
        for (Class<?> serviceType : serviceTypes(service.getClass())) {
            ServiceKey key = new ServiceKey(serviceType, service.serviceId());
            Entry entry = new Entry(pluginId, service, serviceType, source == null ? "classpath" : source);
            Entry existing = services.putIfAbsent(key, entry);
            if (existing != null) {
                unregisterPlugin(pluginId);
                throw new PluginRuntimeException("Duplicate plugin service " + serviceType.getName() + ":" + service.serviceId()
                    + " from " + pluginId + " and " + existing.pluginId);
            }
        }
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        services.entrySet().removeIf(entry -> entry.getValue().pluginId.equals(pluginId));
    }

    @Override
    public <T extends PluginService> Optional<T> find(Class<T> serviceType, String serviceId) {
        Entry entry = services.get(new ServiceKey(serviceType, serviceId));
        return entry == null ? Optional.empty() : Optional.of(serviceType.cast(entry.service));
    }

    @Override
    public <T extends PluginService> List<T> services(Class<T> serviceType) {
        return services.entrySet().stream().filter(entry -> entry.getKey().type.equals(serviceType))
            .map(entry -> serviceType.cast(entry.getValue().service))
            .sorted(Comparator.comparing(PluginService::serviceId)).toList();
    }

    @Override
    public <T extends PluginService, R> R invoke(Class<T> serviceType, String serviceId, Function<T, R> invocation) {
        Entry entry = services.get(new ServiceKey(serviceType, serviceId));
        if (entry == null) throw new PluginRuntimeException("Plugin service not found: " + serviceType.getName() + ":" + serviceId);
        entry.invocations.incrementAndGet();
        entry.lastInvokedAt = Instant.now();
        try {
            R result = invocation.apply(serviceType.cast(entry.service));
            entry.successes.incrementAndGet();
            entry.lastError = "";
            return result;
        } catch (RuntimeException exception) {
            entry.failures.incrementAndGet();
            entry.lastError = exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage();
            throw exception;
        }
    }

    @Override
    public List<PluginServiceSnapshot> snapshots() {
        return services.values().stream().map(Entry::snapshot)
            .sorted(Comparator.comparing(PluginServiceSnapshot::serviceType)
                .thenComparing(PluginServiceSnapshot::serviceId)).toList();
    }

    private static List<Class<?>> serviceTypes(Class<?> implementation) {
        return java.util.Arrays.stream(implementation.getInterfaces())
            .filter(type -> type != PluginService.class && PluginService.class.isAssignableFrom(type))
            .toList();
    }

    private record ServiceKey(Class<?> type, String id) { }

    private static final class Entry {
        private final String pluginId;
        private final PluginService service;
        private final Class<?> serviceType;
        private final String source;
        private final Instant registeredAt = Instant.now();
        private final AtomicLong invocations = new AtomicLong();
        private final AtomicLong successes = new AtomicLong();
        private final AtomicLong failures = new AtomicLong();
        private volatile Instant lastInvokedAt;
        private volatile String lastError = "";

        private Entry(String pluginId, PluginService service, Class<?> serviceType, String source) {
            this.pluginId = pluginId;
            this.service = service;
            this.serviceType = serviceType;
            this.source = source;
        }

        private PluginServiceSnapshot snapshot() {
            return new PluginServiceSnapshot(pluginId, service.serviceId(), serviceType.getName(),
                service.getClass().getName(), source, invocations.get(), successes.get(), failures.get(),
                registeredAt, lastInvokedAt, lastError);
        }
    }
}
