package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.env.Environment;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRuntimeException;

import java.util.List;

public class ProviderBeanValidator implements SmartInitializingSingleton {
    private final ListableBeanFactory beanFactory;
    private final Environment environment;

    public ProviderBeanValidator(ListableBeanFactory beanFactory, Environment environment) {
        this.beanFactory = beanFactory;
        this.environment = environment;
    }

    @Override
    public void afterSingletonsInstantiated() {
        validate("device-registry", DeviceRegistry.class);
        validate("device-session", SessionRepository.class);
        validate("message-bus", DeviceMessageBus.class);
    }

    private void validate(String name, Class<?> contract) {
        String type = environment.getProperty("iot.providers." + name + ".type");
        boolean production = List.of(environment.getActiveProfiles()).stream()
            .anyMatch(profile -> profile.equalsIgnoreCase("prod") || profile.equalsIgnoreCase("production"));
        if ((type == null || type.isBlank()) && !production) return;
        String[] beans = beanFactory.getBeanNamesForType(contract, false, false);
        if (beans.length == 0) {
            throw new PluginRuntimeException("Configured provider has no implementation bean: " + name + "=" + type);
        }
        if (beans.length > 1) {
            throw new PluginRuntimeException("Multiple provider beans found for " + name + ": " + String.join(", ", beans));
        }
    }
}
