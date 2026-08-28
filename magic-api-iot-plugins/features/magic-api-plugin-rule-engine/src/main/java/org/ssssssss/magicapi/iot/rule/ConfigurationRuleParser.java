package org.ssssssss.magicapi.iot.rule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;
import org.ssssssss.magicapi.iot.config.ConfigurationParser;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/** Parses rules/<ruleId> JSON entries from the shared configuration mirror. */
public final class ConfigurationRuleParser implements ConfigurationParser<List<RuleDefinition>> {
    private final ObjectMapper mapper;
    private final InMemoryRuleEngine engine;

    public ConfigurationRuleParser(ObjectMapper mapper, InMemoryRuleEngine engine) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.engine = Objects.requireNonNull(engine, "engine");
    }

    @Override public String id() { return "rule-engine"; }
    @Override public String prefix() { return "rules/"; }

    @Override
    public List<RuleDefinition> parse(Map<String, ConfigurationCenter.ConfigurationValue> values) {
        if (values.isEmpty()) return List.of();
        List<RuleDefinition> rules = new ArrayList<>();
        values.values().stream().sorted(java.util.Comparator.comparing(ConfigurationCenter.ConfigurationValue::key))
            .forEach(value -> rules.add(parse(value.key().substring(prefix().length()), value.value())));
        return List.copyOf(rules);
    }

    @Override public void apply(List<RuleDefinition> parsed) { engine.replace(parsed); }

    @Override
    public Object snapshot(List<RuleDefinition> parsed) {
        return parsed.stream().map(rule -> Map.of(
            "ruleId", rule.ruleId(),
            "priority", rule.priority(),
            "enabled", rule.enabled(),
            "actionId", rule.actionId(),
            "actionConfiguration", rule.actionConfiguration())).toList();
    }

    private RuleDefinition parse(String ruleId, String content) {
        try {
            JsonNode root = mapper.readTree(content);
            String actionId = required(root, "actionId").asText();
            int priority = root.path("priority").asInt(100);
            boolean enabled = root.path("enabled").asBoolean(true);
            Map<String, Object> actionConfiguration = root.has("actionConfiguration")
                ? mapper.convertValue(root.get("actionConfiguration"), Map.class) : Map.of();
            return new RuleDefinition(ruleId, priority, enabled, condition(root.path("condition")),
                actionId, actionConfiguration);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid rule configuration: " + ruleId, exception);
        }
    }

    private Predicate<DeviceMessage> condition(JsonNode condition) {
        if (!condition.isObject() || !condition.has("field")) return ignored -> true;
        String field = condition.get("field").asText();
        JsonNode expected = condition.get("equals");
        return message -> expected == null || expected.asText().equals(fieldValue(message, field));
    }

    private String fieldValue(DeviceMessage message, String field) {
        return switch (field) {
            case "deviceId" -> message.device().deviceId();
            case "productId" -> message.device().productId();
            case "type" -> message.type().name();
            case "protocol" -> message.protocol();
            default -> message.metadata().get(field);
        };
    }

    private static JsonNode required(JsonNode root, String name) {
        JsonNode value = root.get(name);
        if (value == null || value.isNull() || value.asText().isBlank())
            throw new IllegalArgumentException("Missing " + name);
        return value;
    }
}
