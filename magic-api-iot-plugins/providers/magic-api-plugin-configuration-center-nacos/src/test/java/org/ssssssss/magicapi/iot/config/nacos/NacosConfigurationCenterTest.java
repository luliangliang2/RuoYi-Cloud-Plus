package org.ssssssss.magicapi.iot.config.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.filter.IConfigFilter;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NacosConfigurationCenterTest {
    @Test
    void publicNamespaceIsHandledByDefaultTenant() {
        NacosConfigurationCenterProperties properties = new NacosConfigurationCenterProperties();
        assertEquals("public", properties.getNamespace());
    }

    @Test
    void supportsDocumentCasPrefixListAndWatchDiff() {
        InMemoryConfigService configService = new InMemoryConfigService();
        NacosConfigurationCenterProperties properties = new NacosConfigurationCenterProperties();
        try (NacosConfigurationCenter center = new NacosConfigurationCenter(
            configService, new ObjectMapper(), properties)) {
            List<ConfigurationCenter.ConfigurationEvent> events = new ArrayList<>();
            ConfigurationCenter.WatchSubscription watch = center.watch("robot/", events::add);

            var created = center.put("robot/speed", "1.0");
            center.put("other/key", "ignored");

            assertEquals("1.0", center.get("robot/speed").orElseThrow().value());
            assertEquals(List.of("robot/speed"), center.list("robot/").stream()
                .map(ConfigurationCenter.ConfigurationValue::key).toList());
            assertFalse(center.compareAndSet("robot/speed", "nacos:stale", "2.0").applied());

            var updated = center.compareAndSet("robot/speed", center.get("robot/speed").orElseThrow().revision(), "2.0");
            assertTrue(updated.applied());
            assertTrue(center.delete("robot/speed", updated.current().orElseThrow().revision()).applied());
            assertTrue(center.get("robot/speed").isEmpty());
            assertEquals(List.of(ConfigurationCenter.EventType.PUT, ConfigurationCenter.EventType.PUT,
                ConfigurationCenter.EventType.PUT, ConfigurationCenter.EventType.DELETE), events.stream()
                .map(ConfigurationCenter.ConfigurationEvent::type).toList());
            assertFalse(events.get(0).revision().equals(events.get(1).revision()));

            assertFalse(created.revision().equals(updated.current().orElseThrow().revision()));
            watch.close();
            assertTrue(watch.isClosed());
        }
        assertTrue(configService.closed);
    }

    private static final class InMemoryConfigService implements ConfigService {
        private final List<Listener> listeners = new ArrayList<>();
        private String content;
        private boolean closed;

        @Override public String getConfig(String dataId, String group, long timeoutMs) { return content; }
        @Override public String getConfigAndSignListener(String dataId, String group, long timeoutMs, Listener listener) {
            addListener(dataId, group, listener);
            return content;
        }
        @Override public void addListener(String dataId, String group, Listener listener) { listeners.add(listener); }
        @Override public boolean publishConfig(String dataId, String group, String value) {
            return publish(value);
        }
        @Override public boolean publishConfig(String dataId, String group, String value, String type) {
            return publish(value);
        }
        @Override public boolean publishConfigCas(String dataId, String group, String value, String casMd5) {
            return publishCas(value, casMd5);
        }
        @Override public boolean publishConfigCas(String dataId, String group, String value, String casMd5, String type) {
            if (!"json".equals(type)) return false;
            return publishCas(value, casMd5);
        }
        @Override public boolean removeConfig(String dataId, String group) {
            return publish(null);
        }
        @Override public void removeListener(String dataId, String group, Listener listener) { listeners.remove(listener); }
        @Override public String getServerStatus() { return closed ? "DOWN" : "UP"; }
        @Override public void addConfigFilter(IConfigFilter configFilter) { }
        @Override public void shutDown() { closed = true; }

        private boolean publishCas(String value, String casMd5) {
            if (content == null || !md5(content).equals(casMd5)) return false;
            return publish(value);
        }

        private boolean publish(String value) {
            content = value;
            List.copyOf(listeners).forEach(listener -> listener.receiveConfigInfo(value));
            return true;
        }

        private static String md5(String value) {
            try {
                byte[] digest = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
                return HexFormat.of().formatHex(digest);
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        }
    }
}
