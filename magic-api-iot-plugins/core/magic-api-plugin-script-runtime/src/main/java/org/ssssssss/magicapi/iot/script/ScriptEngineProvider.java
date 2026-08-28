package org.ssssssss.magicapi.iot.script;

import org.ssssssss.magicapi.iot.plugin.api.PluginService;

import java.util.Set;

public interface ScriptEngineProvider extends PluginService {
	String engineId();

	Set<String> languages();

	ScriptCompileResult compile(ScriptDefinition definition);

	ScriptExecutionResult execute(ScriptDefinition definition, ScriptExecutionContext context);
}
