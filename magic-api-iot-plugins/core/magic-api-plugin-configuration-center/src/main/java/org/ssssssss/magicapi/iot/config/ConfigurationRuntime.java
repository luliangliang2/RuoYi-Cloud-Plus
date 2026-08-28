package org.ssssssss.magicapi.iot.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ConfigurationRuntime extends AutoCloseable {
    String providerId();

    Optional<ConfigurationCenter.ConfigurationValue> get(String key);

    List<ConfigurationCenter.ConfigurationValue> list(String prefix);

    ConfigurationCenter.ConfigurationValue put(String key, String value);

    ConfigurationCenter.CasResult compareAndSet(String key, String expectedRevision, String value);

    ConfigurationCenter.CasResult delete(String key, String expectedRevision);

    Map<String, Object> parsedSnapshots();

    Map<String, String> parserErrors();

    <T> void registerParser(ConfigurationParser<T> parser);

    @Override
    void close();
}
