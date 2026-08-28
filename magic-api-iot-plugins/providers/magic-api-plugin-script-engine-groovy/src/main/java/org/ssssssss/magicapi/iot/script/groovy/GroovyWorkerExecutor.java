package org.ssssssss.magicapi.iot.script.groovy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.script.*;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/** Executes untrusted Groovy in a separate JVM and enforces parent-side quotas. */
public final class GroovyWorkerExecutor {
    private final ObjectMapper mapper = new ObjectMapper();
    private final int maxSourceBytes;
    private final int maxResponseBytes;
    private final Semaphore concurrency;
    public GroovyWorkerExecutor(int maxConcurrent, int maxSourceBytes, int maxResponseBytes) {
        if (maxConcurrent < 1 || maxSourceBytes < 1 || maxResponseBytes < 1) throw new IllegalArgumentException("worker quotas must be positive");
        this.concurrency = new Semaphore(maxConcurrent); this.maxSourceBytes = maxSourceBytes; this.maxResponseBytes = maxResponseBytes;
    }
    public ScriptExecutionResult execute(ScriptDefinition definition, ScriptExecutionContext context) {
        byte[] source = definition.source().getBytes(StandardCharsets.UTF_8);
        if (source.length > maxSourceBytes) return error("source exceeds worker quota");
        boolean acquired = false;
        try {
            acquired = concurrency.tryAcquire(Math.max(1, Math.min(1000, definition.timeout().toMillis())), TimeUnit.MILLISECONDS);
            if (!acquired) return error("worker concurrency quota exceeded");
            Process process = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-cp", System.getProperty("java.class.path"), GroovyWorkerMain.class.getName()).redirectErrorStream(true).start();
            try (OutputStream output = process.getOutputStream()) {
                output.write(Base64.getEncoder().encode(source)); output.write('\n');
                output.write(Base64.getEncoder().encode(mapper.writeValueAsBytes(context.input()))); output.write('\n');
            }
            if (!process.waitFor(definition.timeout().toMillis(), TimeUnit.MILLISECONDS)) { process.destroyForcibly(); return new ScriptExecutionResult(ScriptExecutionResult.Status.TIMEOUT, Map.of(), java.util.List.of(), java.util.List.of(), "worker timeout", definition.timeout().toMillis()); }
            byte[] response = process.getInputStream().readNBytes(maxResponseBytes + 1);
            if (response.length > maxResponseBytes) return error("worker response exceeds quota");
            if (process.exitValue() != 0) return error(new String(response, StandardCharsets.UTF_8));
            Map<String, Object> envelope = mapper.readValue(response, new TypeReference<>() {});
            ScriptExecutionResult.Status status = ScriptExecutionResult.Status.valueOf(String.valueOf(envelope.getOrDefault("status", "ERROR")));
            @SuppressWarnings("unchecked") Map<String, Object> output = envelope.get("output") instanceof Map<?, ?> values
                ? values.entrySet().stream().collect(java.util.stream.Collectors.toUnmodifiableMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue)) : Map.of();
            return new ScriptExecutionResult(status, output, ScriptActionPlan.from(output.get("value"), context.dryRun()), java.util.List.of(), String.valueOf(envelope.getOrDefault("reason", "")), 0);
        } catch (Exception exception) { return error(exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage()); }
        finally { if (acquired) concurrency.release(); }
    }
    private static ScriptExecutionResult error(String reason) { return new ScriptExecutionResult(ScriptExecutionResult.Status.ERROR, Map.of(), java.util.List.of(), java.util.List.of(), reason, 0); }
}
