package org.ssssssss.magicapi.iot.rule;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;

import java.util.Map;

public interface RuleActionProvider {
    String actionId();

    void execute(DeviceMessage message, Map<String, Object> configuration);
}
