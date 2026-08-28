package org.ssssssss.magicapi.iot.script.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.ssssssss.magicapi.iot.script.ScriptDefinition;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "iot.integration.enabled", matches = "true")
class EtcdScriptRegistryIntegrationTest {
    @Test
    void sharesVersionsBetweenRegistryInstances() {
        String id = "integration-" + UUID.randomUUID();
        EtcdScriptRegistryProperties properties = properties();
        try (Client client = Client.builder().endpoints(properties.getEndpoints().toArray(String[]::new)).build();
             EtcdScriptRegistry first = new EtcdScriptRegistry(client, new ObjectMapper().findAndRegisterModules(), properties)) {
            ScriptDefinition draft = definition(id, 1, ScriptDefinition.Status.DRAFT, false, "return 1;");
            first.save(draft);
            try (Client secondClient = Client.builder().endpoints(properties.getEndpoints().toArray(String[]::new)).build();
                 EtcdScriptRegistry second = new EtcdScriptRegistry(secondClient, new ObjectMapper().findAndRegisterModules(), properties)) {
                assertEquals(draft, second.find(id).orElseThrow());
                assertEquals(ScriptDefinition.Status.PUBLISHED, first.publish(id).status());
                assertEquals(2, second.find(id).orElseThrow().version());
                assertEquals(3, second.rollback(id, 1).version());
                assertEquals("return 1;", first.find(id).orElseThrow().source());
                second.delete(id);
                assertTrue(first.find(id).isEmpty());
            }
        }
    }

    private static ScriptDefinition definition(String id, int version, ScriptDefinition.Status status, boolean enabled, String source) {
        return new ScriptDefinition(id, "integration", "aviator", "aviator", source, version, status, enabled,
                Set.of("iot.device"), Set.of("message.received"), Duration.ofSeconds(2), Map.of("test", true));
    }

    private static EtcdScriptRegistryProperties properties() {
        EtcdScriptRegistryProperties properties = new EtcdScriptRegistryProperties();
        properties.setEndpoints(java.util.List.of(System.getProperty("iot.script.etcd.endpoints",
                "http://10.211.55.4:2379,http://10.211.55.4:22379,http://10.211.55.4:32379").split(",")));
        properties.setRootPrefix("/iot/gateway/scripts/integration/");
        properties.setRequestTimeout(Duration.ofSeconds(5));
        properties.validate();
        return properties;
    }
}
