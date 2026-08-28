package org.ssssssss.magicapi.iot.script;

import java.util.Collection;
import java.util.Map;

public final class ScriptRuntime {
	private final ScriptRegistry registry;
	private final Map<String, ScriptEngineProvider> engines;
	private final ScriptActionExecutorRegistry actionExecutors;
	private final ScriptPermissionPolicy permissionPolicy;

	public ScriptRuntime(ScriptRegistry registry, Collection<ScriptEngineProvider> providers) {
		this(registry, providers, new ScriptActionExecutorRegistry(), (action, context) -> false);
	}

	public ScriptRuntime(ScriptRegistry registry, Collection<ScriptEngineProvider> providers,
			ScriptActionExecutorRegistry actionExecutors, ScriptPermissionPolicy permissionPolicy) {
		this.registry = registry;
		this.actionExecutors = java.util.Objects.requireNonNull(actionExecutors, "actionExecutors");
		this.permissionPolicy = java.util.Objects.requireNonNull(permissionPolicy, "permissionPolicy");
		this.engines = providers.stream()
				.collect(java.util.stream.Collectors.toUnmodifiableMap(ScriptEngineProvider::engineId, p -> p));
	}

	public ScriptCompileResult validate(ScriptDefinition definition) {
		ScriptEngineProvider engine = engine(definition);
		if (definition.source().length() > 100_000)
			return ScriptCompileResult.invalid("source exceeds 100KB");
		if (definition.timeout().isNegative() || definition.timeout().isZero()
				|| definition.timeout().compareTo(java.time.Duration.ofSeconds(30)) > 0)
			return ScriptCompileResult.invalid("timeout must be between 1ms and 30s");
		return engine.compile(definition);
	}

	public ScriptExecutionResult execute(String scriptId, ScriptExecutionContext context) {
		ScriptDefinition definition = registry.find(scriptId)
				.orElseThrow(() -> new IllegalArgumentException("Script not found: " + scriptId));
		if (!definition.enabled() && !context.dryRun())
			return new ScriptExecutionResult(ScriptExecutionResult.Status.REJECTED, Map.of(), java.util.List.of(),
					java.util.List.of(), "Script is disabled", 0);
		ScriptCompileResult compiled = validate(definition);
		if (!compiled.valid())
			return new ScriptExecutionResult(ScriptExecutionResult.Status.ERROR, Map.of(), java.util.List.of(),
					java.util.List.of(), compiled.diagnostics().get(0).message(), 0);
		long started = System.nanoTime();
		try {
			ScriptExecutionResult result = engine(definition).execute(definition, context);
			if (!context.dryRun()) {
				for (ScriptExecutionResult.ScriptAction action : result.actions()) {
					if (!permissionPolicy.allowed(action.actionId(), context))
						return rejected("Action is not allowed: " + action.actionId(), result, started);
					if (!actionExecutors.contains(action.actionId()))
						return rejected("No executor registered for action: " + action.actionId(), result, started);
					actionExecutors.execute(action, context);
				}
			}
			return new ScriptExecutionResult(result.status(), result.output(), result.actions(), result.spiCalls(),
					result.reason(), (System.nanoTime() - started) / 1_000_000);
		} catch (RuntimeException error) {
			return new ScriptExecutionResult(ScriptExecutionResult.Status.ERROR, Map.of(), java.util.List.of(),
					java.util.List.of(), error.getMessage(), (System.nanoTime() - started) / 1_000_000);
		}
	}

	private static ScriptExecutionResult rejected(String reason, ScriptExecutionResult result, long started) {
		return new ScriptExecutionResult(ScriptExecutionResult.Status.REJECTED, result.output(), result.actions(),
				result.spiCalls(), reason, (System.nanoTime() - started) / 1_000_000);
	}

	private ScriptEngineProvider engine(ScriptDefinition definition) {
		ScriptEngineProvider p = engines.get(definition.engineId());
		if (p == null)
			throw new IllegalArgumentException("Script engine not found: " + definition.engineId());
		return p;
	}
}
