package org.ssssssss.magicapi.iot.script.groovy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.script.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

/** One-request worker protocol. The process exits after one execution. */
public final class GroovyWorkerMain {
    private GroovyWorkerMain() { }
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String source = new String(Base64.getDecoder().decode(input.readLine()), StandardCharsets.UTF_8);
            Map<String, Object> values = mapper.readValue(input.readLine(), new TypeReference<>() {});
            ScriptDefinition definition = new ScriptDefinition("worker", "worker", "groovy", "groovy", source, 1, ScriptDefinition.Status.PUBLISHED, true, java.util.Set.of(), java.util.Set.of(), Duration.ofSeconds(30), Map.of());
            ScriptExecutionResult result = new GroovyScriptEngineProvider().execute(definition, new ScriptExecutionContext("worker", "WORKER", values, Map.of(), true));
            System.out.write(mapper.writeValueAsBytes(Map.of("status", result.status().name(), "output", result.output(), "reason", result.reason())));
        }
    }
}
