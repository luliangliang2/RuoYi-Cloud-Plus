package org.ssssssss.magicapi.iot.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Maintenance operations intentionally use SCAN; the runtime search path does not. */
public final class RedisDeviceIndexMaintenance {
    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public RedisDeviceIndexMaintenance(StringRedisTemplate redis, ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }

    public Result rebuild() {
        Set<String> all = new HashSet<>();
        Set<String> productIndexes = new HashSet<>();
        scan("iot:device:*", key -> {
            if (key.contains(":credential:") || key.startsWith("iot:device:index:")) return;
            String value = redis.opsForValue().get(key);
            if (value == null) return;
            try {
                RegisteredDevice device = mapper.readValue(value, RegisteredDevice.class);
                all.add(device.identity().routingKey());
                productIndexes.add("iot:device:index:product:" + device.identity().productId());
            } catch (Exception ignored) {
                // Invalid records remain visible for separate registry diagnostics.
            }
        });
        redis.delete("iot:device:index:all");
        scan("iot:device:index:product:*", productIndexes::add);
        productIndexes.forEach(redis::delete);
        all.forEach(routingKey -> {
            String[] parts = routingKey.split("/", 2);
            redis.opsForSet().add("iot:device:index:all", routingKey);
            if (parts.length == 2) redis.opsForSet().add("iot:device:index:product:" + parts[0], routingKey);
        });
        return new Result(all.size(), 0, all.size());
    }

    public Result verify() {
        Set<String> expectedAll = new HashSet<>();
        Set<String> expectedProduct = new HashSet<>();
        scan("iot:device:*", key -> {
            if (key.contains(":credential:") || key.startsWith("iot:device:index:")) return;
            String value = redis.opsForValue().get(key);
            if (value == null) return;
            try {
                RegisteredDevice device = mapper.readValue(value, RegisteredDevice.class);
                expectedAll.add(device.identity().routingKey());
                expectedProduct.add(device.identity().productId() + "\u0000" + device.identity().routingKey());
            } catch (Exception ignored) { }
        });
        Set<String> actualAll = redis.opsForSet().members("iot:device:index:all");
        actualAll = actualAll == null ? Set.of() : actualAll;
        int missing = difference(expectedAll, actualAll).size();
        int stale = difference(actualAll, expectedAll).size();
        int productErrors = 0;
        for (String item : expectedProduct) {
            String[] parts = item.split("\u0000", 2);
            Set<String> members = redis.opsForSet().members("iot:device:index:product:" + parts[0]);
            if (members == null || !members.contains(parts[1])) productErrors++;
        }
        return new Result(expectedAll.size(), missing + productErrors, stale);
    }

    public Result repair() {
        Result before = verify();
        return before.missing() == 0 && before.stale() == 0 ? before : rebuild();
    }

    private void scan(String pattern, java.util.function.Consumer<String> consumer) {
        redis.execute((org.springframework.data.redis.core.RedisCallback<Void>) connection -> {
            try (var cursor = connection.scan(org.springframework.data.redis.core.ScanOptions.scanOptions()
                    .match(pattern).count(500).build())) {
                while (cursor.hasNext()) consumer.accept(new String(cursor.next(), java.nio.charset.StandardCharsets.UTF_8));
            }
            return null;
        });
    }

    private static Set<String> difference(Set<String> left, Set<String> right) {
        Set<String> result = new HashSet<>(left);
        result.removeAll(right);
        return result;
    }

    public record Result(int devices, int missing, int stale) {
        public boolean consistent() { return missing == 0 && stale == 0; }
    }
}
