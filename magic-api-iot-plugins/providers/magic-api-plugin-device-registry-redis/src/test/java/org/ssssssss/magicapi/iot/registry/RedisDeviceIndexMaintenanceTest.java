package org.ssssssss.magicapi.iot.registry;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RedisDeviceIndexMaintenanceTest {
    @Test
    void resultReportsConsistency() {
        assertTrue(new RedisDeviceIndexMaintenance.Result(1, 0, 0).consistent());
    }
}
