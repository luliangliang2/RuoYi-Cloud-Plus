package org.ssssssss.magicapi.iot.script;

import java.util.List;
import java.util.Map;

public final class ScriptActionPlan {
	private ScriptActionPlan() {
	}

	public static List<ScriptExecutionResult.ScriptAction> from(Object value, boolean dryRun) {
		if (!(value instanceof Map<?, ?> map) || !(map.get("actions") instanceof Iterable<?> actions))
			return List.of();
		java.util.ArrayList<ScriptExecutionResult.ScriptAction> result = new java.util.ArrayList<>();
		for (Object raw : actions) {
			if (!(raw instanceof Map<?, ?> action) || action.get("actionId") == null)
				continue;
			String actionId = action.get("actionId").toString();
			if (actionId.isBlank())
				continue;
			Map<String, Object> parameters = action.get("parameters") instanceof Map<?, ?> values
					? values.entrySet().stream()
							.collect(java.util.stream.Collectors
									.toUnmodifiableMap(entry -> String.valueOf(entry.getKey()), Map.Entry::getValue))
					: Map.of();
			result.add(new ScriptExecutionResult.ScriptAction(actionId, parameters, dryRun));
		}
		return List.copyOf(result);
	}
}
