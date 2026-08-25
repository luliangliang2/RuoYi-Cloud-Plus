package org.ssssssss.magicapi.iot.plugin.api;

public interface IotPlugin {

    PluginDescriptor descriptor();

    default void initialize(PluginContext context) {
    }

    default void start() {
    }

    default PluginHealth health() {
        return PluginHealth.up();
    }

    default void stop() {
    }
}
