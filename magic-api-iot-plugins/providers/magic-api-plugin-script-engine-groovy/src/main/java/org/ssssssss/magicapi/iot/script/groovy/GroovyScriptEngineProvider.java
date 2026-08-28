package org.ssssssss.magicapi.iot.script.groovy;

import groovy.lang.*;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.ssssssss.magicapi.iot.script.*;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

public final class GroovyScriptEngineProvider implements ScriptEngineProvider {
	private final GroovyWorkerExecutor worker;

	public GroovyScriptEngineProvider() { this(null); }

	public GroovyScriptEngineProvider(GroovyWorkerExecutor worker) { this.worker = worker; }
	private static final Set<String> FORBIDDEN = Set.of("java.io", "java.net", "java.lang.reflect", "ProcessBuilder",
			"Runtime", "ClassLoader", "System.exit", "System.getProperties", "System.getenv");

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
			newShell().parse(definition.source());
			return ScriptCompileResult.success();
		} catch (RuntimeException e) {
			return ScriptCompileResult.invalid(e.getMessage());
		}
	}

	public ScriptExecutionResult execute(ScriptDefinition definition, ScriptExecutionContext context) {
		if (worker != null) return worker.execute(definition, context);
		long start = System.nanoTime();
		try {
			Binding binding = new Binding();
			binding.setVariable("input", context.input());
			binding.setVariable("attributes", context.attributes());
			Object value = newShell(binding).evaluate(definition.source());
			return new ScriptExecutionResult(ScriptExecutionResult.Status.SUCCESS,
					Map.of("value", value == null ? "" : value), ScriptActionPlan.from(value, context.dryRun()), java.util.List.of(), "",
					(System.nanoTime() - start) / 1_000_000);
		} catch (RuntimeException e) {
			return new ScriptExecutionResult(ScriptExecutionResult.Status.ERROR, Map.of(), java.util.List.of(),
					java.util.List.of(), e.getMessage(), (System.nanoTime() - start) / 1_000_000);
		}
	}

	private static GroovyShell newShell() {
		return new GroovyShell(sandboxConfiguration());
	}

	private static GroovyShell newShell(Binding binding) {
		return new GroovyShell(GroovyScriptEngineProvider.class.getClassLoader(), binding, sandboxConfiguration());
	}

	private static CompilerConfiguration sandboxConfiguration() {
		SecureASTCustomizer secure = new SecureASTCustomizer();
		secure.setClosuresAllowed(false);
		secure.setMethodDefinitionAllowed(false);
		secure.setImportsWhitelist(List.of(
			"java.util.Map", "java.util.List", "java.util.Set", "java.util.Collections"));
		secure.setStaticImportsWhitelist(List.of());
		secure.setStaticStarImportsWhitelist(List.of());
		secure.setReceiversBlackList(Arrays.asList(
			"java.lang.System", "java.lang.Runtime", "java.lang.ProcessBuilder", "java.lang.Class",
			"java.lang.ClassLoader", "java.lang.reflect.Method", "java.lang.reflect.Field",
			"java.io.File", "java.net.URL", "java.net.Socket", "java.lang.Thread"));
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.addCompilationCustomizers(secure);
		return configuration;
	}
}
