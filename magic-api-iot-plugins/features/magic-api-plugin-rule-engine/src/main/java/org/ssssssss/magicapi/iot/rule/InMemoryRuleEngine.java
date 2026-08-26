package org.ssssssss.magicapi.iot.rule;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InMemoryRuleEngine implements RuleEngine {
    private final Map<String, RuleActionProvider> actions;
    private volatile List<RuleDefinition> rules = List.of();

    public InMemoryRuleEngine(Collection<RuleActionProvider> actions) {
        try {
            this.actions = actions.stream().collect(Collectors.toUnmodifiableMap(
                RuleActionProvider::actionId, Function.identity()));
        } catch (IllegalStateException exception) {
            throw new IllegalArgumentException("Duplicate rule action provider", exception);
        }
    }

    public void replace(Collection<RuleDefinition> rules) {
        rules.forEach(rule -> requiredAction(rule.actionId()));
        this.rules = rules.stream().sorted(Comparator.comparingInt(RuleDefinition::priority)).toList();
    }

    public int evaluate(DeviceMessage message) {
        int matched = 0;
        for (RuleDefinition rule : rules) {
            if (rule.enabled() && rule.condition().test(message)) {
                requiredAction(rule.actionId()).execute(message, rule.actionConfiguration());
                matched++;
            }
        }
        return matched;
    }

    private RuleActionProvider requiredAction(String actionId) {
        RuleActionProvider action = actions.get(actionId);
        if (action == null) {
            throw new IllegalArgumentException("No rule action provider: " + actionId);
        }
        return action;
    }
}
