package org.ssssssss.magicapi.iot.script;

import java.util.Map;

@FunctionalInterface
public interface ScriptPermissionPolicy {
    boolean allowed(String actionId, ScriptExecutionContext context);

    static ScriptPermissionPolicy allowListed(Map<String, Boolean> permissions) {
        Map<String, Boolean> values = permissions == null ? Map.of() : Map.copyOf(permissions);
        return (actionId, ignored) -> Boolean.TRUE.equals(values.get(actionId));
    }
}
