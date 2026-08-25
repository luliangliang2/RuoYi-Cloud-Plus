package org.ssssssss.magicapi.iot.plugin.api;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

public interface PluginContext {

    String pluginId();

    Map<String, Object> configuration();

    <T> Optional<T> service(Class<T> serviceType);

    ScheduledExecutorService scheduler();

    Path dataDirectory();
}
