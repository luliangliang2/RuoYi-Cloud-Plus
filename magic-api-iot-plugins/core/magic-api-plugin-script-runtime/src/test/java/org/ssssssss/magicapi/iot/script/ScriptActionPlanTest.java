package org.ssssssss.magicapi.iot.script;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScriptActionPlanTest {
    @Test
    void convertsActionDslAndPreservesDryRun() {
        var value = Map.of("actions", List.of(Map.of(
                "actionId", "route.bind",
                "parameters", Map.of("deviceId", "agv-001"))));

        var actions = ScriptActionPlan.from(value, true);

        assertEquals(1, actions.size());
        assertEquals("route.bind", actions.get(0).actionId());
        assertEquals("agv-001", actions.get(0).parameters().get("deviceId"));
        assertTrue(actions.get(0).dryRun());
    }

    @Test
    void ignoresMalformedActionsAndNonDslValues() {
        var value = Map.of("actions", List.of(
                Map.of("actionId", ""),
                Map.of("parameters", Map.of("x", 1)),
                "not-an-action"));

        assertTrue(ScriptActionPlan.from(value, false).isEmpty());
        assertTrue(ScriptActionPlan.from("true", false).isEmpty());
    }
}
