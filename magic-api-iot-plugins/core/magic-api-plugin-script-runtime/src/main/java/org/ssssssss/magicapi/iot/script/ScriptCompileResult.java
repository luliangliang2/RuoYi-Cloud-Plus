package org.ssssssss.magicapi.iot.script;

import java.util.List;

public record ScriptCompileResult(boolean valid, List<Diagnostic> diagnostics) {
	public ScriptCompileResult {
		diagnostics = diagnostics == null ? List.of() : List.copyOf(diagnostics);
	}

	public static ScriptCompileResult success() {
		return new ScriptCompileResult(true, List.of());
	}

	public static ScriptCompileResult invalid(String message) {
		return new ScriptCompileResult(false, List.of(new Diagnostic("ERROR", message, 0, 0)));
	}

	public record Diagnostic(String severity, String message, int line, int column) {
	}
}
