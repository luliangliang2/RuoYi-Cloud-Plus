package org.ssssssss.magicapi.iot.script.graalvm;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.script.ScriptDefinition;
import org.ssssssss.magicapi.iot.script.ScriptExecutionContext;
import org.ssssssss.magicapi.iot.script.ScriptExecutionResult;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraalVmScriptEngineProviderTest {
    private final GraalVmScriptEngineProvider provider = new GraalVmScriptEngineProvider();

    @Test
    void readsNestedInputWithoutHostAccessAndBuildsActionPlan() {
        var definition = new ScriptDefinition("auth-js", "Auth", "graalvm", "javascript",
                "({actions:[{actionId:'device.authenticate',parameters:{deviceId:input.device.id,tags:input.tags}}]})",
                1, null, true, Set.of("device.authenticate"), Set.of("AUTHENTICATE"),
                Duration.ofSeconds(2), Map.of());
        var context = new ScriptExecutionContext("trace-1", "AUTHENTICATE",
                Map.of("device", Map.of("id", "agv-001"), "tags", java.util.List.of("robot")), Map.of(), true);

        var result = provider.execute(definition, context);

        assertEquals(ScriptExecutionResult.Status.SUCCESS, result.status(), result.reason());
        assertEquals(1, result.actions().size());
        assertEquals("agv-001", result.actions().get(0).parameters().get("deviceId"));
        assertTrue(result.actions().get(0).dryRun());
    }
}
