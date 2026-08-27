package org.ssssssss.magicapi.iot.registry;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryDeviceRegistryAdminTest {

    @Test
    void managesRegistrationStatusAndCredentialsWithoutExposingSecrets() {
        InMemoryDeviceRegistry registry = new InMemoryDeviceRegistry();
        DeviceIdentity identity = new DeviceIdentity("robot", "agv-001");
        registry.save(new RegisteredDevice(identity, true, "raw", Map.of("lift", "supported"), 1));
        registry.setCredential(identity, new DeviceCredential("secret", "value-1"));

        var page = registry.search("robot", "agv", 1, 20);
        assertEquals(1, page.total());
        assertEquals(identity, page.items().get(0).identity());
        assertEquals(java.util.Set.of("secret"), registry.credentialTypes(identity));
        assertTrue(registry.verifyCredential(identity, new DeviceCredential("secret", "value-1")));

        RegisteredDevice disabled = registry.setEnabled(identity, false);
        assertFalse(disabled.enabled());
        assertEquals(2, disabled.version());
        assertFalse(registry.verifyCredential(identity, new DeviceCredential("secret", "value-1")));

        registry.deleteCredential(identity, "secret");
        assertTrue(registry.credentialTypes(identity).isEmpty());
    }

    @Test
    void supportsExplicitCreateUpdateAndDeleteLifecycle() {
        InMemoryDeviceRegistry registry = new InMemoryDeviceRegistry();
        DeviceIdentity identity = new DeviceIdentity("robot", "agv-002");
        RegisteredDevice created = new RegisteredDevice(identity, true, "raw", Map.of(), 1);
        assertEquals(created, registry.register(created));
        assertThrows(IllegalArgumentException.class, () -> registry.register(created));

        RegisteredDevice updated = new RegisteredDevice(identity, false, "mqtt", Map.of("telemetry", "supported"), 2);
        assertEquals(updated, registry.update(updated));
        assertEquals(updated, registry.find(identity).orElseThrow());

        registry.setCredential(identity, new DeviceCredential("secret", "value-2"));
        registry.delete(identity);
        assertTrue(registry.find(identity).isEmpty());
        assertTrue(registry.credentialTypes(identity).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> registry.delete(identity));
    }
}
