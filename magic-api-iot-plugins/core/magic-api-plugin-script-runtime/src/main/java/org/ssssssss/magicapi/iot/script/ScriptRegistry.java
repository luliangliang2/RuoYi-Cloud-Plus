package org.ssssssss.magicapi.iot.script;

import java.util.List;
import java.util.Optional;

public interface ScriptRegistry {
	List<ScriptDefinition> list();

	Optional<ScriptDefinition> find(String scriptId);

	ScriptDefinition save(ScriptDefinition definition);

	ScriptDefinition publish(String scriptId);

	ScriptDefinition setEnabled(String scriptId, boolean enabled);

	ScriptDefinition rollback(String scriptId, int version);

	void delete(String scriptId);
}
