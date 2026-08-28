package org.ssssssss.magicapi.iot.config;

import java.util.Map;

/** Converts a namespaced configuration snapshot into a runtime object. */
public interface ConfigurationParser<T> {
    String id();

    String prefix();

    T parse(Map<String, ConfigurationCenter.ConfigurationValue> values);

    default void apply(T parsed) { }

    /** Only explicitly refreshable parsers may mutate a running provider after startup. */
    default boolean refreshable() { return false; }

    default Object snapshot(T parsed) { return parsed; }
}
