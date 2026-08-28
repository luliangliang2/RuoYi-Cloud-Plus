package org.ssssssss.magicapi.iot.rule;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import java.util.Collection;

public interface RuleEngine {
    void replace(Collection<RuleDefinition> rules);
    int evaluate(DeviceMessage message);
}
