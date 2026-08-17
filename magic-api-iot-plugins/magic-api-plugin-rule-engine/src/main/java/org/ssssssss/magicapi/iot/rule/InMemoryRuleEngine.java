package org.ssssssss.magicapi.iot.rule;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class InMemoryRuleEngine implements RuleEngine {
    private volatile List<DeviceRule> rules = List.of();

    public void replace(Collection<DeviceRule> rules) {
        this.rules = rules.stream().sorted(Comparator.comparingInt(DeviceRule::priority)).toList();
    }

    public int evaluate(DeviceMessage message) {
        int matched = 0;
        for (DeviceRule rule : rules) {
            if (rule.enabled() && rule.condition().test(message)) {
                rule.action().accept(message);
                matched++;
            }
        }
        return matched;
    }
}

