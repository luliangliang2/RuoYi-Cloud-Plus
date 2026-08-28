package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.PulsarClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.ssssssss.magicapi.iot.core.model.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "iot.integration.enabled", matches = "true")
class PulsarDeviceMessageBusIntegrationTest {
    @Test void publishesConsumesAndRedeliversDeviceMessage() throws Exception {
        String serviceUrl = System.getProperty("iot.pulsar.service-url", "pulsar://10.211.55.4:6650");
        String suffix = UUID.randomUUID().toString();
        String topic = "persistent://public/default/iot-test-device-" + suffix;
        PulsarClient client = PulsarClient.builder().serviceUrl(serviceUrl).build();
        PulsarDeviceMessageBus bus = new PulsarDeviceMessageBus(client,
            new ObjectMapper().findAndRegisterModules(), topic, 3, 100, 10);
        AtomicInteger attempts = new AtomicInteger();
        CountDownLatch received = new CountDownLatch(1);
        var subscription = bus.subscribe("integration-" + suffix, message -> {
            if (attempts.getAndIncrement() == 0) throw new IllegalStateException("expected redelivery");
            received.countDown();
        });
        try {
            bus.publish(new DeviceMessage(null, new DeviceIdentity("iot-test", suffix),
                DeviceMessageType.EVENT_REPORT, "integration", null, null, Map.of("value", 1), Map.of()));
            assertTrue(received.await(15, TimeUnit.SECONDS), "Pulsar consumer did not receive the redelivered message");
            assertTrue(attempts.get() >= 2, "Pulsar did not redeliver the failed message");
        } finally {
            subscription.close();
            bus.close();
            client.close();
        }
    }
}
