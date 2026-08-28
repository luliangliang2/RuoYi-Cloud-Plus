package org.ssssssss.magicapi.iot.script;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

public record ScriptDefinition(String scriptId, String name, String engineId, String language, String source,
		int version, Status status, boolean enabled, Set<String> permissions, Set<String> triggers, Duration timeout,
		Map<String, Object> metadata) {
	public ScriptDefinition {
		if (scriptId == null || scriptId.isBlank())
			throw new IllegalArgumentException("scriptId must not be blank");
		if (name == null || name.isBlank())
			throw new IllegalArgumentException("name must not be blank");
		if (engineId == null || engineId.isBlank())
			throw new IllegalArgumentException("engineId must not be blank");
		source = source == null ? "" : source;
		version = Math.max(1, version);
		status = status == null ? Status.DRAFT : status;
		permissions = permissions == null ? Set.of() : Set.copyOf(permissions);
		triggers = triggers == null ? Set.of() : Set.copyOf(triggers);
		timeout = timeout == null ? Duration.ofSeconds(2) : timeout;
		metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
	}

	public enum Status {
		DRAFT, VALID, PUBLISHED, DISABLED, FAILED
	}
}
