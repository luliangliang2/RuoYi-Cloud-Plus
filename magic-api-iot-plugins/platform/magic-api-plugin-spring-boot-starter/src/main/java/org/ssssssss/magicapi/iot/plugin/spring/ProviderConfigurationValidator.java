package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.core.env.Environment;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRuntimeException;

import java.util.List;

public class ProviderConfigurationValidator {

    private static final List<String> PROVIDERS = List.of("device-registry", "device-session", "message-bus");

    public void validate(Environment environment) {
        boolean production = List.of(environment.getActiveProfiles()).stream()
            .map(String::toLowerCase)
            .anyMatch(profile -> profile.equals("prod") || profile.equals("production"));
        if (!production) return;
        for (String provider : PROVIDERS) {
            String type = environment.getProperty("iot.providers." + provider + ".type");
            if ("memory".equalsIgnoreCase(type)) {
                throw new PluginRuntimeException(
                    "In-memory provider is forbidden in production: iot.providers." + provider + ".type");
            }
        }
    }
}
