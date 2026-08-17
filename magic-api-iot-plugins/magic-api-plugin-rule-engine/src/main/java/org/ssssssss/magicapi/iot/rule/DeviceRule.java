package org.ssssssss.magicapi.iot.rule;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import java.util.function.Consumer;
import java.util.function.Predicate;

public record DeviceRule(String ruleId, int priority, boolean enabled,
                         Predicate<DeviceMessage> condition, Consumer<DeviceMessage> action) {
}

