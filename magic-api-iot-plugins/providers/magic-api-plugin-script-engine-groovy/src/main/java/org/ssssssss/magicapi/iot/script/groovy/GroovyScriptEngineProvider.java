package org.ssssssss.magicapi.iot.script.groovy;

import groovy.lang.*;
import org.ssssssss.magicapi.iot.script.*;
import java.util.Map;
import java.util.Set;

public final class GroovyScriptEngineProvider implements ScriptEngineProvider {
	private static final Set<String> FORBIDDEN = Set.of("java.io", "java.net", "java.lang.reflect", "ProcessBuilder",
			"Runtime", "ClassLoader", "System.exit");

	public String serviceId() {
		return "groovy";
	}

	public String engineId() {
		return "groovy";
	}

	public Set<String> languages() {
		return Set.of("groovy");
	}

	public ScriptCompileResult compile(ScriptDefinition definition) {
		String forbidden = FORBIDDEN.stream().filter(definition.source()::contains).findFirst().orElse(null);
		if (forbidden != null)
			return ScriptCompileResult.invalid("forbidden token: " + forbidden);
		try {
			new GroovyShell().parse(definition.source());
			return ScriptCompileResult.success();
		} catch (RuntimeException e) {
			return ScriptCompileResult.invalid(e.getMessage());
		}
	}

	public ScriptExecutionResult execute(ScriptDefinition definition, ScriptExecutionContext context) {
		long start = System.nanoTime();
		try {
			Binding binding = new Binding();
			binding.setVariable("input", context.input());
			binding.setVariable("attributes", context.attributes());
			Object value = new GroovyShell(binding).evaluate(definition.source());
			return new ScriptExecutionResult(ScriptExecutionResult.Status.SUCCESS,
					Map.of("value", value == null ? "" : value), ScriptActionPlan.from(value, context.dryRun()), java.util.List.of(), "",
					(System.nanoTime() - start) / 1_000_000);
		} catch (RuntimeException e) {
			return new ScriptExecutionResult(ScriptExecutionResult.Status.ERROR, Map.of(), java.util.List.of(),
					java.util.List.of(), e.getMessage(), (System.nanoTime() - start) / 1_000_000);
		}
	}
}
