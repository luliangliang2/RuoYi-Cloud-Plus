package org.ssssssss.magicapi.iot.script.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import org.ssssssss.magicapi.iot.script.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AviatorScriptEngineProvider implements ScriptEngineProvider {
    public String serviceId() { return "aviator"; }
    public String engineId() { return "aviator"; }
    public Set<String> languages() { return Set.of("aviator"); }
    public ScriptCompileResult compile(ScriptDefinition definition) { try { AviatorEvaluator.compile(definition.scriptId(), definition.source(), true); return ScriptCompileResult.success(); } catch (RuntimeException e) { return ScriptCompileResult.invalid(e.getMessage()); } }
    public ScriptExecutionResult execute(ScriptDefinition definition, ScriptExecutionContext context) { long start = System.nanoTime(); Object value = AviatorEvaluator.execute(definition.source(), context.input()); return new ScriptExecutionResult(ScriptExecutionResult.Status.SUCCESS, Map.of("value", value == null ? "" : value), ScriptActionPlan.from(value, context.dryRun()), List.of(), "", (System.nanoTime() - start) / 1_000_000); }
}
