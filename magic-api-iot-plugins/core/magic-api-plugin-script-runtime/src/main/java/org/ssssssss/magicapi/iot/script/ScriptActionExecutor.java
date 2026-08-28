package org.ssssssss.magicapi.iot.script;

@FunctionalInterface
public interface ScriptActionExecutor {
    void execute(ScriptExecutionResult.ScriptAction action, ScriptExecutionContext context);
}
