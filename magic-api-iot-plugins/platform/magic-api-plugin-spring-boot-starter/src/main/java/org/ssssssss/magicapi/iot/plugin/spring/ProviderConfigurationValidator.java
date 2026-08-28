package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.core.env.Environment;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRuntimeException;

import java.util.List;
import java.util.Map;

public class ProviderConfigurationValidator {

    private static final List<String> PROVIDERS = List.of("device-registry", "device-session", "message-bus",
        "node-registry", "configuration-center");
    private static final Map<String, List<String>> ALLOWED_TYPES = Map.of(
        "device-registry", List.of("memory", "redis", "jdbc"),
        "device-session", List.of("memory", "redis"),
        "message-bus", List.of("memory", "kafka", "pulsar", "rocketmq"),
        "node-registry", List.of("nacos", "zookeeper", "etcd"),
        "configuration-center", List.of("nacos", "zookeeper", "etcd"));

    public void validate(Environment environment) {
        boolean production = List.of(environment.getActiveProfiles()).stream()
            .map(String::toLowerCase)
            .anyMatch(profile -> profile.equals("prod") || profile.equals("production"));
        for (String provider : PROVIDERS) {
            String type = environment.getProperty("iot.providers." + provider + ".type");
            if (type == null || type.isBlank()) {
                if (production) {
                    throw new PluginRuntimeException("Missing required provider type: iot.providers." + provider + ".type");
                }
                continue;
            }
            if (production && "memory".equalsIgnoreCase(type)) {
                throw new PluginRuntimeException(
                    "In-memory provider is forbidden in production: iot.providers." + provider + ".type");
            }
            if (!ALLOWED_TYPES.get(provider).contains(type.toLowerCase())) {
                throw new PluginRuntimeException("Unsupported provider type: " + type + " for " + provider);
            }
        }
    }
}
