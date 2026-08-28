package org.ssssssss.magicapi.iot.script;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryScriptRegistryTest {
    @Test void publishesDisablesAndRollsBackVersions() {
        var registry = new InMemoryScriptRegistry();
        var draft = new ScriptDefinition("route-1", "Route", "aviator", "aviator", "true", 1, null, false, Set.of(), Set.of("MESSAGE"), Duration.ofSeconds(1), Map.of());
        registry.save(draft); assertEquals(ScriptDefinition.Status.PUBLISHED, registry.publish("route-1").status());
        assertFalse(registry.setEnabled("route-1", false).enabled());
        assertEquals(4, registry.rollback("route-1", 1).version());
    }
}
