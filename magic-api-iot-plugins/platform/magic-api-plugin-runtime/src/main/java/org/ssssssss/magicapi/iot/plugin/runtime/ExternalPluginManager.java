package org.ssssssss.magicapi.iot.plugin.runtime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.plugin.api.IotPlugin;
import org.ssssssss.magicapi.iot.plugin.api.PluginContext;
import org.ssssssss.magicapi.iot.plugin.api.PluginDescriptor;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.PluginService;
import org.ssssssss.magicapi.iot.plugin.api.PluginState;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.jar.JarFile;

public final class ExternalPluginManager implements AutoCloseable {
    private static final String DESCRIPTOR = "META-INF/iot-plugin.json";
    private static final List<String> SHARED_API_PREFIXES = List.of(
        "org/ssssssss/magicapi/iot/plugin/api/",
        "org/ssssssss/magicapi/iot/core/model/",
        "org/ssssssss/magicapi/iot/core/spi/");

    private final Path pluginDirectory;
    private final Path dataDirectory;
    private final PluginRegistry pluginRegistry;
    private final CapabilityRegistry capabilityRegistry;
    private final PluginServiceRegistry serviceRegistry;
    private final ClassLoader parentClassLoader;
    private final Map<Class<?>, Object> sharedServices;
    private final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final Map<String, LoadedPlugin> loaded = new HashMap<>();
    private final Map<String, String> discoveryErrors = new HashMap<>();
    private final Set<Path> disabled = new HashSet<>();

    public ExternalPluginManager(Path pluginDirectory, Path dataDirectory, PluginRegistry pluginRegistry,
                                 CapabilityRegistry capabilityRegistry, PluginServiceRegistry serviceRegistry,
                                 ClassLoader parentClassLoader, Map<Class<?>, Object> sharedServices) {
        this.pluginDirectory = pluginDirectory.toAbsolutePath().normalize();
        this.dataDirectory = dataDirectory.toAbsolutePath().normalize();
        this.pluginRegistry = pluginRegistry;
        this.capabilityRegistry = capabilityRegistry;
        this.serviceRegistry = serviceRegistry;
        this.parentClassLoader = parentClassLoader;
        this.sharedServices = Map.copyOf(sharedServices == null ? Map.of() : sharedServices);
    }

    public synchronized void start() {
        try {
            Files.createDirectories(pluginDirectory);
            Files.createDirectories(dataDirectory);
        } catch (IOException exception) {
            throw new PluginRuntimeException("Failed to initialize external plugin directories", exception);
        }
        rescan();
    }

    public synchronized void rescan() {
        discoveryErrors.clear();
        Set<Path> discovered = new HashSet<>();
        try (var stream = Files.list(pluginDirectory)) {
            stream.filter(path -> path.getFileName().toString().endsWith(".jar"))
                .map(path -> path.toAbsolutePath().normalize()).sorted().forEach(discovered::add);
        } catch (IOException exception) {
            throw new PluginRuntimeException("Failed to scan external plugin directory: " + pluginDirectory, exception);
        }
        new ArrayList<>(loaded.values()).stream().filter(plugin -> !discovered.contains(plugin.jar))
            .map(plugin -> plugin.descriptor.id()).toList().forEach(this::disable);
        for (Path jar : discovered) {
            if (disabled.contains(jar) || loaded.values().stream().anyMatch(plugin -> plugin.jar.equals(jar))) continue;
            try {
                load(jar);
            } catch (RuntimeException exception) {
                discoveryErrors.put(jar.toString(), message(exception));
            }
        }
    }

    public synchronized ExternalPluginSnapshot enable(Path jar) {
        Path normalized = jar.toAbsolutePath().normalize();
        requireInsidePluginDirectory(normalized);
        disabled.remove(normalized);
        return load(normalized).snapshot();
    }

    public synchronized ExternalPluginValidation validate(Path jar) {
        Path normalized = jar.toAbsolutePath().normalize();
        requireInsidePluginDirectory(normalized);
        validateJarContents(normalized);
        PluginDescriptor descriptor = readDescriptor(normalized);
        List<String> missingDependencies = descriptor.requires().stream()
            .filter(dependency -> pluginRegistry.find(dependency).isEmpty()).toList();
        List<String> services;
        try (URLClassLoader classLoader = new URLClassLoader("iot-plugin-validation-" + descriptor.id(),
            new java.net.URL[]{normalized.toUri().toURL()}, parentClassLoader)) {
            services = ServiceLoader.load(PluginService.class, classLoader).stream()
                .filter(provider -> provider.type().getClassLoader() == classLoader)
                .map(provider -> provider.type().getName()).sorted().toList();
        } catch (IOException | RuntimeException exception) {
            throw new PluginRuntimeException("Failed to validate external plugin: " + normalized, exception);
        }
        boolean duplicate = loaded.containsKey(descriptor.id()) || pluginRegistry.find(descriptor.id()).isPresent();
        return new ExternalPluginValidation(descriptor.id(), descriptor.version(), normalized.toString(),
            missingDependencies.isEmpty(), duplicate, missingDependencies, services);
    }

    public synchronized void disable(String pluginId) {
        LoadedPlugin plugin = loaded.remove(pluginId);
        if (plugin == null) return;
        disabled.add(plugin.jar);
        stopAndRelease(plugin);
    }

    public synchronized ExternalPluginSnapshot reload(String pluginId) {
        LoadedPlugin current = requireLoaded(pluginId);
        Path jar = current.jar;
        stopAndRelease(current);
        loaded.remove(pluginId);
        disabled.remove(jar);
        return load(jar).snapshot();
    }

    public synchronized ExternalPluginSnapshot upgrade(String pluginId, Path stagedJar) {
        LoadedPlugin current = requireLoaded(pluginId);
        Path normalizedStagedJar = stagedJar.toAbsolutePath().normalize();
        requireInsidePluginDirectory(normalizedStagedJar);
        Path target = current.jar;
        Path backup = backupPath(target);
        Path staging = pluginDirectory.resolve(target.getFileName() + ".upgrade");
        try {
            Files.copy(normalizedStagedJar, staging, StandardCopyOption.REPLACE_EXISTING);
            readDescriptor(staging);
            Files.copy(target, backup, StandardCopyOption.REPLACE_EXISTING);
            stopAndRelease(current);
            loaded.remove(pluginId);
            move(staging, target);
            return load(target).snapshot();
        } catch (Exception exception) {
            try {
                if (Files.exists(backup)) Files.copy(backup, target, StandardCopyOption.REPLACE_EXISTING);
                if (!loaded.containsKey(pluginId)) load(target);
            } catch (Exception rollbackFailure) {
                exception.addSuppressed(rollbackFailure);
            }
            throw new PluginRuntimeException("Plugin upgrade failed and rollback was attempted: " + pluginId, exception);
        }
    }

    public synchronized ExternalPluginSnapshot rollback(String pluginId) {
        LoadedPlugin current = requireLoaded(pluginId);
        Path backup = backupPath(current.jar);
        if (!Files.exists(backup)) throw new PluginRuntimeException("No rollback artifact for plugin: " + pluginId);
        Path currentJar = current.jar;
        stopAndRelease(current);
        loaded.remove(pluginId);
        try {
            Files.copy(backup, currentJar, StandardCopyOption.REPLACE_EXISTING);
            return load(currentJar).snapshot();
        } catch (IOException exception) {
            throw new PluginRuntimeException("Failed to rollback plugin: " + pluginId, exception);
        }
    }

    public synchronized List<ExternalPluginSnapshot> snapshots() {
        return loaded.values().stream().map(LoadedPlugin::snapshot)
            .sorted(Comparator.comparing(ExternalPluginSnapshot::pluginId)).toList();
    }

    public synchronized Map<String, String> discoveryErrors() {
        return Map.copyOf(discoveryErrors);
    }

    public Path pluginDirectory() { return pluginDirectory; }

    @Override
    public synchronized void close() {
        new ArrayList<>(loaded.values()).forEach(this::stopAndRelease);
        loaded.clear();
    }

    private LoadedPlugin load(Path jar) {
        requireInsidePluginDirectory(jar);
        validateJarContents(jar);
        PluginDescriptor descriptor = readDescriptor(jar);
        if (loaded.containsKey(descriptor.id()) || pluginRegistry.find(descriptor.id()).isPresent())
            throw new PluginRuntimeException("Duplicate plugin id: " + descriptor.id());
        for (String dependency : descriptor.requires()) {
            if (pluginRegistry.find(dependency).isEmpty())
                throw new PluginRuntimeException("Plugin " + descriptor.id() + " requires missing plugin " + dependency);
        }

        URLClassLoader classLoader;
        try {
            classLoader = new URLClassLoader("iot-plugin-" + descriptor.id(),
                new java.net.URL[]{jar.toUri().toURL()}, parentClassLoader);
        } catch (IOException exception) {
            throw new PluginRuntimeException("Failed to create plugin class loader: " + jar, exception);
        }
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "iot-plugin-" + descriptor.id());
            thread.setDaemon(true);
            thread.setContextClassLoader(classLoader);
            return thread;
        });
        try {
            List<IotPlugin> entrypoints = ServiceLoader.load(IotPlugin.class, classLoader).stream()
                .filter(provider -> provider.type().getClassLoader() == classLoader)
                .map(ServiceLoader.Provider::get).toList();
            if (entrypoints.size() > 1)
                throw new PluginRuntimeException("External plugin must expose at most one IotPlugin entrypoint: " + descriptor.id());
            IotPlugin entrypoint = entrypoints.isEmpty() ? new DescriptorOnlyPlugin(descriptor) : entrypoints.get(0);
            if (!descriptor.id().equals(entrypoint.descriptor().id()))
                throw new PluginRuntimeException("Plugin descriptor id does not match IotPlugin entrypoint: " + descriptor.id());
            List<PluginService> services = ServiceLoader.load(PluginService.class, classLoader).stream()
                .filter(provider -> provider.type().getClassLoader() == classLoader)
                .map(ServiceLoader.Provider::get).toList();
            PluginContext context = new DefaultContext(descriptor.id(), scheduler,
                dataDirectory.resolve(descriptor.id()), sharedServices);

            pluginRegistry.register(descriptor);
            capabilityRegistry.register(descriptor.id(), descriptor.capabilities());
            pluginRegistry.update(descriptor.id(), PluginState.INITIALIZED,
                new PluginHealth(PluginHealth.Status.UNKNOWN, "Plugin initialized", Map.of()), "");
            entrypoint.initialize(context);
            for (PluginService service : services) {
                service.initialize(context);
                serviceRegistry.register(descriptor.id(), service, jar.toString());
            }
            pluginRegistry.update(descriptor.id(), PluginState.STARTING,
                new PluginHealth(PluginHealth.Status.UNKNOWN, "Plugin starting", Map.of()), "");
            entrypoint.start();
            PluginHealth health = entrypoint.health();
            PluginState state = health.status() == PluginHealth.Status.UP ? PluginState.RUNNING : PluginState.DEGRADED;
            pluginRegistry.update(descriptor.id(), state, health, "");
            LoadedPlugin loadedPlugin = new LoadedPlugin(descriptor, jar, classLoader, scheduler, entrypoint,
                services, Instant.now(), state, "");
            loaded.put(descriptor.id(), loadedPlugin);
            return loadedPlugin;
        } catch (RuntimeException exception) {
            serviceRegistry.unregisterPlugin(descriptor.id());
            capabilityRegistry.unregister(descriptor.id());
            pluginRegistry.unregister(descriptor.id());
            scheduler.shutdownNow();
            closeQuietly(classLoader);
            throw exception;
        }
    }

    private void stopAndRelease(LoadedPlugin plugin) {
        try {
            plugin.state = PluginState.STOPPING;
            pluginRegistry.update(plugin.descriptor.id(), PluginState.STOPPING,
                new PluginHealth(PluginHealth.Status.UNKNOWN, "Plugin stopping", Map.of()), "");
            plugin.services.forEach(service -> {
                try { service.stop(); } catch (RuntimeException ignored) { }
            });
            plugin.entrypoint.stop();
            plugin.state = PluginState.STOPPED;
        } finally {
            serviceRegistry.unregisterPlugin(plugin.descriptor.id());
            capabilityRegistry.unregister(plugin.descriptor.id());
            pluginRegistry.unregister(plugin.descriptor.id());
            plugin.scheduler.shutdownNow();
            closeQuietly(plugin.classLoader);
        }
    }

    private PluginDescriptor readDescriptor(Path jar) {
        try (JarFile file = new JarFile(jar.toFile())) {
            var entry = file.getJarEntry(DESCRIPTOR);
            if (entry == null) throw new PluginRuntimeException("External plugin descriptor is missing: " + jar);
            try (var input = file.getInputStream(entry)) {
                PluginDescriptor descriptor = mapper.readValue(input, PluginDescriptor.class);
                new PluginDescriptorValidator().validate(List.of(descriptor));
                return descriptor;
            }
        } catch (IOException exception) {
            throw new PluginRuntimeException("Failed to read external plugin descriptor: " + jar, exception);
        }
    }

    private void validateJarContents(Path jar) {
        try (JarFile file = new JarFile(jar.toFile())) {
            Optional<String> bundledApi = file.stream().map(java.util.jar.JarEntry::getName)
                .filter(name -> SHARED_API_PREFIXES.stream().anyMatch(name::startsWith)).findFirst();
            if (bundledApi.isPresent())
                throw new PluginRuntimeException("External plugin must not bundle shared API class: " + bundledApi.orElseThrow());
        } catch (IOException exception) {
            throw new PluginRuntimeException("Invalid external plugin JAR: " + jar, exception);
        }
    }

    private LoadedPlugin requireLoaded(String pluginId) {
        LoadedPlugin plugin = loaded.get(pluginId);
        if (plugin == null) throw new PluginRuntimeException("External plugin is not loaded: " + pluginId);
        return plugin;
    }

    private void requireInsidePluginDirectory(Path path) {
        if (!path.toAbsolutePath().normalize().startsWith(pluginDirectory))
            throw new PluginRuntimeException("Plugin path must be inside " + pluginDirectory + ": " + path);
        if (!Files.isRegularFile(path)) throw new PluginRuntimeException("Plugin JAR does not exist: " + path);
    }

    private static Path backupPath(Path jar) {
        return jar.resolveSibling(jar.getFileName() + ".previous");
    }

    private static void move(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ignored) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void closeQuietly(URLClassLoader classLoader) {
        try { classLoader.close(); } catch (IOException ignored) { }
    }

    private static String message(Throwable error) {
        return error.getMessage() == null ? error.getClass().getName() : error.getMessage();
    }

    private static final class LoadedPlugin {
        private final PluginDescriptor descriptor;
        private final Path jar;
        private final URLClassLoader classLoader;
        private final ScheduledExecutorService scheduler;
        private final IotPlugin entrypoint;
        private final List<PluginService> services;
        private final Instant loadedAt;
        private volatile PluginState state;
        private volatile String lastError;

        private LoadedPlugin(PluginDescriptor descriptor, Path jar, URLClassLoader classLoader,
                             ScheduledExecutorService scheduler, IotPlugin entrypoint,
                             List<PluginService> services, Instant loadedAt, PluginState state,
                             String lastError) {
            this.descriptor = descriptor;
            this.jar = jar;
            this.classLoader = classLoader;
            this.scheduler = scheduler;
            this.entrypoint = entrypoint;
            this.services = List.copyOf(services);
            this.loadedAt = loadedAt;
            this.state = state;
            this.lastError = lastError == null ? "" : lastError;
        }

        private ExternalPluginSnapshot snapshot() {
            return new ExternalPluginSnapshot(descriptor.id(), descriptor.version(), jar.toString(),
                classLoader.getName() == null ? classLoader.toString() : classLoader.getName(),
                state, services.stream().map(service -> service.getClass().getName()).toList(),
                loadedAt, lastError);
        }
    }

    private record DefaultContext(String pluginId, ScheduledExecutorService scheduler, Path dataDirectory,
                                  Map<Class<?>, Object> sharedServices) implements PluginContext {
        @Override public Map<String, Object> configuration() { return Map.of(); }
        @Override public <T> Optional<T> service(Class<T> serviceType) {
            return Optional.ofNullable(sharedServices.get(serviceType)).map(serviceType::cast);
        }
    }

    private record DescriptorOnlyPlugin(PluginDescriptor descriptor) implements IotPlugin { }
}
