package org.ssssssss.magicapi.iot.plugin.api;

/** Marker for services that an external IoT plugin may expose through ServiceLoader. */
public interface PluginService {

    String serviceId();

    default String ownerPluginId() { return "classpath-services"; }

    default void initialize(PluginContext context) { }

    default void stop() { }
}
