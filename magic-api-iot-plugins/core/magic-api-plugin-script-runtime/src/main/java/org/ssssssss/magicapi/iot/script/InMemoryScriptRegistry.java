package org.ssssssss.magicapi.iot.script;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryScriptRegistry implements ScriptRegistry {
	private final ConcurrentHashMap<String, ScriptDefinition> current = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, List<ScriptDefinition>> history = new ConcurrentHashMap<>();

	@Override
	public List<ScriptDefinition> list() {
		return current.values().stream().sorted(Comparator.comparing(ScriptDefinition::scriptId)).toList();
	}

	@Override
	public Optional<ScriptDefinition> find(String scriptId) {
		return Optional.ofNullable(current.get(scriptId));
	}

	@Override
	public ScriptDefinition save(ScriptDefinition definition) {
		current.put(definition.scriptId(), definition);
		history.computeIfAbsent(definition.scriptId(), ignored -> new ArrayList<>()).add(definition);
		return definition;
	}

	@Override
	public ScriptDefinition publish(String scriptId) {
		return change(scriptId, ScriptDefinition.Status.PUBLISHED, true);
	}

	@Override
	public ScriptDefinition setEnabled(String scriptId, boolean enabled) {
		return change(scriptId, enabled ? ScriptDefinition.Status.PUBLISHED : ScriptDefinition.Status.DISABLED,
				enabled);
	}

	@Override
	public ScriptDefinition rollback(String scriptId, int version) {
		ScriptDefinition found = history.getOrDefault(scriptId, List.of()).stream()
				.filter(item -> item.version() == version).findFirst().orElseThrow(
						() -> new IllegalArgumentException("Script version not found: " + scriptId + "/" + version));
		return save(new ScriptDefinition(found.scriptId(), found.name(), found.engineId(), found.language(),
				found.source(), current.get(scriptId).version() + 1, ScriptDefinition.Status.PUBLISHED, true,
				found.permissions(), found.triggers(), found.timeout(), found.metadata()));
	}

	@Override
	public void delete(String scriptId) {
		if (current.remove(scriptId) == null)
			throw new IllegalArgumentException("Script not found: " + scriptId);
	}

	private ScriptDefinition change(String id, ScriptDefinition.Status status, boolean enabled) {
		ScriptDefinition old = current.get(id);
		if (old == null)
			throw new IllegalArgumentException("Script not found: " + id);
		return save(new ScriptDefinition(old.scriptId(), old.name(), old.engineId(), old.language(), old.source(),
				old.version() + 1, status, enabled, old.permissions(), old.triggers(), old.timeout(), old.metadata()));
	}
}
