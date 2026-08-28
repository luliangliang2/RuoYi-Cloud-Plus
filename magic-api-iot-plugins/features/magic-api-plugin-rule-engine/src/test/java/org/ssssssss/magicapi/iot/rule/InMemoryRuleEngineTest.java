package org.ssssssss.magicapi.iot.rule;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryRuleEngineTest {
    @Test void resolvesActionByStableId() {
        AtomicInteger calls = new AtomicInteger();
        RuleActionProvider action = new RuleActionProvider() {
            public String actionId() { return "alarm"; }
            public void execute(DeviceMessage message, Map<String, Object> configuration) { calls.incrementAndGet(); }
        };
        InMemoryRuleEngine engine = new InMemoryRuleEngine(List.of(action));
        engine.replace(List.of(new RuleDefinition("r1", 1, true, ignored -> true, "alarm", Map.of())));

        assertEquals(1, engine.evaluate(message()));
        assertEquals(1, calls.get());
    }

    @Test void rejectsMissingActionDuringRuleReplacement() {
        InMemoryRuleEngine engine = new InMemoryRuleEngine(List.of());
        assertThrows(IllegalArgumentException.class, () -> engine.replace(List.of(
            new RuleDefinition("r1", 1, true, ignored -> true, "missing", Map.of()))));
    }

    private DeviceMessage message() {
        return new DeviceMessage(null, new DeviceIdentity("p", "d"), DeviceMessageType.EVENT_REPORT,
            "test", null, null, Map.of(), Map.of());
    }
}
