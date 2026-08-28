package org.ssssssss.magicapi.iot.script.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.op.Cmp;
import io.etcd.jetcd.op.CmpTarget;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.GetOption;
import org.ssssssss.magicapi.iot.script.ScriptDefinition;
import org.ssssssss.magicapi.iot.script.ScriptRegistry;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class EtcdScriptRegistry implements ScriptRegistry, AutoCloseable {
    private final Client client;
    private final ObjectMapper mapper;
    private final EtcdScriptRegistryProperties properties;

    public EtcdScriptRegistry(Client client, ObjectMapper mapper, EtcdScriptRegistryProperties properties) {
        this.client = Objects.requireNonNull(client, "client");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.properties = Objects.requireNonNull(properties, "properties");
    }

    @Override
    public List<ScriptDefinition> list() {
        try {
            var response = client.getKVClient().get(bytes(properties.getRootPrefix()),
                    GetOption.builder().isPrefix(true).build()).get(timeout(), TimeUnit.MILLISECONDS);
            return response.getKvs().stream().filter(kv -> decode(kv.getKey()).endsWith("/current"))
                    .map(this::read).sorted(Comparator.comparing(ScriptDefinition::scriptId)).toList();
        } catch (Exception e) { throw failure("list scripts", e); }
    }

    @Override
    public Optional<ScriptDefinition> find(String scriptId) {
        keyPart(scriptId);
        try {
            var response = client.getKVClient().get(currentKey(scriptId)).get(timeout(), TimeUnit.MILLISECONDS);
            return response.getKvs().stream().findFirst().map(this::read);
        } catch (Exception e) { throw failure("read script " + scriptId, e); }
    }

    @Override
    public ScriptDefinition save(ScriptDefinition definition) {
        Objects.requireNonNull(definition, "definition");
        String id = keyPart(definition.scriptId());
        try {
            var current = client.getKVClient().get(currentKey(definition.scriptId())).get(timeout(), TimeUnit.MILLISECONDS);
            long currentRevision = current.getKvs().stream().findFirst().map(KeyValue::getModRevision).orElse(0L);
            int actualVersion = current.getKvs().stream().findFirst().map(this::read).map(ScriptDefinition::version).orElse(0);
            if (definition.version() != actualVersion + 1)
                throw new IllegalStateException("Script version conflict: expected=" + (actualVersion + 1) + ", actual=" + definition.version());
            var transaction = client.getKVClient().txn()
                    .If(currentRevision == 0 ? new Cmp(currentKey(definition.scriptId()), Cmp.Op.EQUAL, CmpTarget.version(0)) : new Cmp(currentKey(definition.scriptId()), Cmp.Op.EQUAL, CmpTarget.modRevision(currentRevision)),
                            new Cmp(versionKey(definition.scriptId(), definition.version()), Cmp.Op.EQUAL, CmpTarget.version(0)))
                    .Then(Op.put(versionKey(definition.scriptId(), definition.version()), bytes(write(definition)), io.etcd.jetcd.options.PutOption.DEFAULT),
                            Op.put(currentKey(definition.scriptId()), bytes(write(definition)), io.etcd.jetcd.options.PutOption.DEFAULT))
                    .commit().get(timeout(), TimeUnit.MILLISECONDS);
            if (!transaction.isSucceeded()) throw new IllegalStateException("Script version conflict: concurrent update detected");
            return definition;
        } catch (IllegalStateException e) { throw e; }
        catch (Exception e) { throw failure("save script " + id, e); }
    }

    @Override public ScriptDefinition publish(String scriptId) { return change(scriptId, ScriptDefinition.Status.PUBLISHED, true); }

    @Override public ScriptDefinition setEnabled(String scriptId, boolean enabled) {
        return change(scriptId, enabled ? ScriptDefinition.Status.PUBLISHED : ScriptDefinition.Status.DISABLED, enabled);
    }

    @Override
    public ScriptDefinition rollback(String scriptId, int version) {
        keyPart(scriptId);
        try {
            var response = client.getKVClient().get(versionKey(scriptId, version)).get(timeout(), TimeUnit.MILLISECONDS);
            ScriptDefinition old = response.getKvs().stream().findFirst().map(this::read)
                    .orElseThrow(() -> new IllegalArgumentException("Script version not found: " + scriptId + "/" + version));
            ScriptDefinition current = find(scriptId).orElseThrow(() -> new IllegalArgumentException("Script not found: " + scriptId));
            return save(copy(old, current.version() + 1, ScriptDefinition.Status.PUBLISHED, true));
        } catch (IllegalArgumentException | IllegalStateException e) { throw e; }
        catch (Exception e) { throw failure("rollback script " + scriptId, e); }
    }

    @Override
    public void delete(String scriptId) {
        keyPart(scriptId);
        try { client.getKVClient().delete(bytes(scriptPrefix(scriptId)), io.etcd.jetcd.options.DeleteOption.builder().isPrefix(true).build()).get(timeout(), TimeUnit.MILLISECONDS); }
        catch (Exception e) { throw failure("delete script " + scriptId, e); }
    }

    public boolean isAvailable() {
        try { client.getKVClient().get(bytes(properties.getRootPrefix()), GetOption.builder().withLimit(1).isPrefix(true).build()).get(timeout(), TimeUnit.MILLISECONDS); return true; }
        catch (Exception e) { return false; }
    }

    private ScriptDefinition change(String id, ScriptDefinition.Status status, boolean enabled) {
        ScriptDefinition current = find(id).orElseThrow(() -> new IllegalArgumentException("Script not found: " + id));
        return save(copy(current, current.version() + 1, status, enabled));
    }

    private ScriptDefinition copy(ScriptDefinition source, int version, ScriptDefinition.Status status, boolean enabled) {
        return new ScriptDefinition(source.scriptId(), source.name(), source.engineId(), source.language(), source.source(), version, status, enabled, source.permissions(), source.triggers(), source.timeout(), source.metadata());
    }
    private ScriptDefinition read(KeyValue kv) { try { return mapper.readValue(kv.getValue().toString(StandardCharsets.UTF_8), ScriptDefinition.class); } catch (Exception e) { throw new IllegalStateException("Invalid script record: " + decode(kv.getKey()), e); } }
    private String write(ScriptDefinition definition) { try { return mapper.writeValueAsString(definition); } catch (Exception e) { throw new IllegalStateException("Failed to serialize script: " + definition.scriptId(), e); } }
    private String keyPart(String id) { if (id == null || id.isBlank()) throw new IllegalArgumentException("scriptId must not be blank"); return Base64.getUrlEncoder().withoutPadding().encodeToString(id.getBytes(StandardCharsets.UTF_8)); }
    private ByteSequence currentKey(String id) { return bytes(scriptPrefix(id) + "current"); }
    private ByteSequence versionKey(String id, int version) { if (version < 1) throw new IllegalArgumentException("version must be positive"); return bytes(scriptPrefix(id) + "versions/" + version); }
    private String scriptPrefix(String id) { return properties.getRootPrefix() + keyPart(id) + "/"; }
    private String decode(ByteSequence key) { return key.toString(StandardCharsets.UTF_8); }
    private static ByteSequence bytes(String value) { return ByteSequence.from(value, StandardCharsets.UTF_8); }
    private long timeout() { return properties.getRequestTimeout().toMillis(); }
    private static IllegalStateException failure(String operation, Exception e) { return new IllegalStateException("Failed to " + operation + " in etcd", e); }
    @Override public void close() { client.close(); }
}
