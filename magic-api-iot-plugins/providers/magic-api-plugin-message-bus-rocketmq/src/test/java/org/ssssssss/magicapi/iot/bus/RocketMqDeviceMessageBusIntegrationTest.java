package org.ssssssss.magicapi.iot.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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
class RocketMqDeviceMessageBusIntegrationTest {

    @Test void publishesConsumesAndRetriesDeviceMessage() throws Exception {
        String nameServer = System.getProperty("iot.rocketmq.name-server", "10.211.55.4:9876");
        String suffix = UUID.randomUUID().toString().replace("-", "");
        String topic = "iot_test_device_" + suffix;
        DefaultMQProducer producer = new DefaultMQProducer("iot_test_producer_" + suffix);
        producer.setNamesrvAddr(nameServer);
        producer.start();
        RocketMQTemplate template = new RocketMQTemplate();
        template.setProducer(producer);
        RocketMqDeviceMessageBus bus = new RocketMqDeviceMessageBus(template,
            new ObjectMapper().findAndRegisterModules(), nameServer, topic, 3);
        CountDownLatch received = new CountDownLatch(1);
        AtomicInteger attempts = new AtomicInteger();
        var subscription = bus.subscribe("integration-" + suffix, message -> {
            if (attempts.getAndIncrement() == 0) throw new IllegalStateException("expected retry");
            received.countDown();
        });
        try {
            Thread.sleep(1500);
            bus.publish(new DeviceMessage(null, new DeviceIdentity("iot-test", suffix),
                DeviceMessageType.EVENT_REPORT, "integration", null, null, Map.of("value", 1), Map.of()));
            assertTrue(received.await(20, TimeUnit.SECONDS), "RocketMQ consumer did not receive the retried message");
            assertTrue(attempts.get() >= 2, "RocketMQ did not redeliver the failed message");
        } finally {
            subscription.close();
            producer.shutdown();
        }
    }
}
