package org.ssssssss.magicapi.iot.rule;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public record RuleDefinition(
    String ruleId,
    int priority,
    boolean enabled,
    Predicate<DeviceMessage> condition,
    String actionId,
    Map<String, Object> actionConfiguration
) {
    public RuleDefinition {
        Objects.requireNonNull(ruleId, "ruleId");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(actionId, "actionId");
        actionConfiguration = actionConfiguration == null ? Map.of() : Map.copyOf(actionConfiguration);
    }
}
