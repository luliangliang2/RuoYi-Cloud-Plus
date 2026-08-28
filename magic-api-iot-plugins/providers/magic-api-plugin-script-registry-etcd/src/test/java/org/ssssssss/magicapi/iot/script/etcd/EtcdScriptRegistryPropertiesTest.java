package org.ssssssss.magicapi.iot.script.etcd;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EtcdScriptRegistryPropertiesTest {
    @Test
    void acceptsClusterEndpointsAndDefaults() {
        EtcdScriptRegistryProperties properties = new EtcdScriptRegistryProperties();
        properties.setEndpoints(List.of("http://10.211.55.4:2379", "http://10.211.55.4:22379", "http://10.211.55.4:32379"));
        assertDoesNotThrow(properties::validate);
    }

    @Test
    void rejectsInvalidPrefixAndPartialCredentials() {
        EtcdScriptRegistryProperties properties = new EtcdScriptRegistryProperties();
        properties.setRootPrefix("iot/scripts");
        assertThrows(IllegalStateException.class, properties::validate);

        properties = new EtcdScriptRegistryProperties();
        properties.setUsername("user");
        properties.setPassword("");
        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void rejectsNonPositiveTimeout() {
        EtcdScriptRegistryProperties properties = new EtcdScriptRegistryProperties();
        properties.setRequestTimeout(Duration.ZERO);
        assertThrows(IllegalStateException.class, properties::validate);
    }
}
