package org.ssssssss.magicapi.iot.config.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZookeeperConfigurationCenterTest {
    @Test
    void supportsNativeVersionCasAndWatch() throws Exception {
        try (TestingServer server = new TestingServer()) {
            CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new RetryOneTime(50));
            client.start();
            assertTrue(client.blockUntilConnected(3, TimeUnit.SECONDS));
            ZookeeperConfigurationCenterProperties properties = new ZookeeperConfigurationCenterProperties();
            properties.setRootPath("/test/config");
            try (ZookeeperConfigurationCenter center = new ZookeeperConfigurationCenter(client, properties)) {
                List<ConfigurationCenter.ConfigurationEvent> events = new CopyOnWriteArrayList<>();
                ConfigurationCenter.WatchSubscription watch = center.watch("robot/", events::add);
                var created = center.put("robot/speed", "1.0");
                assertEquals("1.0", center.get("robot/speed").orElseThrow().value());
                assertEquals(1, center.list("robot/").size());
                assertFalse(center.compareAndSet("robot/speed", "zookeeper:99:99", "2.0").applied());
                var updated = center.compareAndSet("robot/speed", created.revision(), "2.0");
                assertTrue(updated.applied());
                awaitEvents(events, 2);
                assertTrue(center.delete("robot/speed", updated.current().orElseThrow().revision()).applied());
                awaitEvents(events, 3);
                assertEquals(List.of(ConfigurationCenter.EventType.PUT, ConfigurationCenter.EventType.PUT,
                    ConfigurationCenter.EventType.DELETE), events.stream().map(ConfigurationCenter.ConfigurationEvent::type).toList());
                watch.close();
                assertTrue(watch.isClosed());
            }
        }
    }

    private static void awaitEvents(List<?> events, int count) throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(3).toNanos();
        while (events.size() < count && System.nanoTime() < deadline) Thread.sleep(10);
        assertEquals(count, events.size());
    }
}
