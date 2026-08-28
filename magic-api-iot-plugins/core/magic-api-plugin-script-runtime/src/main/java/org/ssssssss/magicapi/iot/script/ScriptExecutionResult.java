package org.ssssssss.magicapi.iot.script;

import java.util.List;
import java.util.Map;

public record ScriptExecutionResult(Status status, Map<String, Object> output, List<ScriptAction> actions,
		List<SpiCall> spiCalls, String reason, long durationMs) {
	public ScriptExecutionResult {
		output = output == null ? Map.of() : Map.copyOf(output);
		actions = actions == null ? List.of() : List.copyOf(actions);
		spiCalls = spiCalls == null ? List.of() : List.copyOf(spiCalls);
		reason = reason == null ? "" : reason;
	}

	public enum Status {
		SUCCESS, REJECTED, TIMEOUT, ERROR
	}

	public record ScriptAction(String actionId, Map<String, Object> parameters, boolean dryRun) {
	}

	public record SpiCall(String serviceId, String providerId, long durationMs, String status) {
	}
}
