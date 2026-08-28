package org.ssssssss.magicapi.iot.script.graalvm;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.ssssssss.magicapi.iot.script.*;
import java.util.Map;
import java.util.List;
import java.util.Set;

public final class GraalVmScriptEngineProvider implements ScriptEngineProvider {
	public String serviceId() {
		return "graalvm-js";
	}

	public String engineId() {
		return "graalvm";
	}

	public Set<String> languages() {
		return Set.of("javascript", "js");
	}

	public ScriptCompileResult compile(ScriptDefinition definition) {
		try (Context context = context()) {
			context.parse("js", definition.source());
			return ScriptCompileResult.success();
		} catch (RuntimeException e) {
			return ScriptCompileResult.invalid(e.getMessage());
		}
	}

	public ScriptExecutionResult execute(ScriptDefinition definition, ScriptExecutionContext execution) {
		long start = System.nanoTime();
		try (Context context = context()) {
			context.getBindings("js").putMember("input", toGuest(execution.input()));
			context.getBindings("js").putMember("attributes", toGuest(execution.attributes()));
			Value result = context.eval("js", definition.source());
			Object value = toJava(result);
			return new ScriptExecutionResult(ScriptExecutionResult.Status.SUCCESS,
					Map.of("value", value == null ? "" : value), ScriptActionPlan.from(value, execution.dryRun()), List.of(), "",
					(System.nanoTime() - start) / 1_000_000);
		} catch (RuntimeException e) {
			return new ScriptExecutionResult(ScriptExecutionResult.Status.ERROR, Map.of(), List.of(), List.of(),
					errorMessage(e), (System.nanoTime() - start) / 1_000_000);
		}
	}

	private static Context context() {
		return Context.newBuilder("js").allowHostAccess(HostAccess.NONE).allowHostClassLookup(name -> false)
				.allowIO(false).allowNativeAccess(false).allowCreateThread(false).build();
	}

	private static Object toJava(Value value) {
		if (value == null || value.isNull()) return null;
		if (value.isBoolean()) return value.asBoolean();
		if (value.isNumber()) return value.fitsInLong() ? value.asLong() : value.asDouble();
		if (value.isString()) return value.asString();
		if (value.hasArrayElements()) {
			java.util.ArrayList<Object> items = new java.util.ArrayList<>();
			for (long index = 0; index < value.getArraySize(); index++) items.add(toJava(value.getArrayElement(index)));
			return java.util.List.copyOf(items);
		}
		if (value.hasMembers()) {
			java.util.LinkedHashMap<String, Object> map = new java.util.LinkedHashMap<>();
			for (String key : value.getMemberKeys()) map.put(key, toJava(value.getMember(key)));
			return java.util.Map.copyOf(map);
		}
		return value.toString();
	}

	private static Object toGuest(Object value) {
		if (value instanceof Map<?, ?> map) {
			java.util.LinkedHashMap<String, Object> converted = new java.util.LinkedHashMap<>();
			map.forEach((key, item) -> converted.put(String.valueOf(key), toGuest(item)));
			return ProxyObject.fromMap(converted);
		}
		if (value instanceof Iterable<?> iterable) {
			java.util.ArrayList<Object> converted = new java.util.ArrayList<>();
			iterable.forEach(item -> converted.add(toGuest(item)));
			return ProxyArray.fromList(converted);
		}
		if (value != null && value.getClass().isArray()) {
			int length = java.lang.reflect.Array.getLength(value);
			java.util.ArrayList<Object> converted = new java.util.ArrayList<>(length);
			for (int index = 0; index < length; index++) converted.add(toGuest(java.lang.reflect.Array.get(value, index)));
			return ProxyArray.fromList(converted);
		}
		return value;
	}

	private static String errorMessage(RuntimeException error) {
		return error.getMessage() == null || error.getMessage().isBlank()
				? error.getClass().getSimpleName()
				: error.getMessage();
	}
}
