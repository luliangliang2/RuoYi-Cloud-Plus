package org.ssssssss.magicapi.iot.script;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ScriptActionExecutorRegistry {
    private final Map<String, ScriptActionExecutor> executors = new ConcurrentHashMap<>();

    public void register(String actionId, ScriptActionExecutor executor) {
        if (actionId == null || actionId.isBlank()) throw new IllegalArgumentException("actionId must not be blank");
        executors.put(actionId, Objects.requireNonNull(executor, "executor"));
    }

    public boolean contains(String actionId) { return executors.containsKey(actionId); }

    public void execute(ScriptExecutionResult.ScriptAction action, ScriptExecutionContext context) {
        ScriptActionExecutor executor = executors.get(action.actionId());
        if (executor == null) throw new IllegalArgumentException("No executor registered for action: " + action.actionId());
        executor.execute(action, context);
    }
}
