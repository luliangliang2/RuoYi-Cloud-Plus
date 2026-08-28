package org.ssssssss.magicapi.iot.plugin.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;
import org.ssssssss.magicapi.iot.config.ConfigurationParser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Generic JSON namespace parser used by provider, transport and protocol runtime configuration. */
public final class JsonNamespaceConfigurationParser implements ConfigurationParser<Map<String, Object>> {
    private final String id;
    private final String prefix;
    private final ObjectMapper mapper;

    public JsonNamespaceConfigurationParser(String id, String prefix, ObjectMapper mapper) {
        this.id = Objects.requireNonNull(id, "id");
        this.prefix = Objects.requireNonNull(prefix, "prefix");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override public String id() { return id; }
    @Override public String prefix() { return prefix; }

    @Override
    public Map<String, Object> parse(Map<String, ConfigurationCenter.ConfigurationValue> values) {
        Map<String, Object> result = new LinkedHashMap<>();
        values.values().stream().sorted(java.util.Comparator.comparing(ConfigurationCenter.ConfigurationValue::key))
            .forEach(value -> result.put(value.key().substring(prefix.length()), parseValue(value)));
        return Map.copyOf(result);
    }

    private Object parseValue(ConfigurationCenter.ConfigurationValue value) {
        try {
            return mapper.readValue(value.value(), Object.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid JSON configuration: " + value.key(), exception);
        }
    }
}
