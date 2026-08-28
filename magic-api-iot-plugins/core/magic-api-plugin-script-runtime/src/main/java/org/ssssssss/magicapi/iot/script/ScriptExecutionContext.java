package org.ssssssss.magicapi.iot.script;

import java.util.Map;

public record ScriptExecutionContext(String traceId, String trigger, Map<String, Object> input,
		Map<String, Object> attributes, boolean dryRun) {
	public ScriptExecutionContext {
		traceId = traceId == null || traceId.isBlank() ? "script-debug" : traceId;
		trigger = trigger == null ? "MANUAL" : trigger;
		input = input == null ? Map.of() : Map.copyOf(input);
		attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
	}
}
