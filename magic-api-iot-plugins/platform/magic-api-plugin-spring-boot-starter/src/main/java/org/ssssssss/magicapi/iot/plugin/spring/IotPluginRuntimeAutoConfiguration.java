package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.env.Environment;
import org.ssssssss.magicapi.iot.plugin.runtime.CapabilityRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.ClasspathPluginDescriptorLoader;
import org.ssssssss.magicapi.iot.plugin.runtime.DefaultCapabilityRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.DefaultPluginRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginDescriptorValidator;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.util.List;

@AutoConfiguration
public class IotPluginRuntimeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    CapabilityRegistry iotCapabilityRegistry() {
        return new DefaultCapabilityRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    PluginRegistry iotPluginRegistry(CapabilityRegistry capabilityRegistry, Environment environment) {
        new ProviderConfigurationValidator().validate(environment);
        PluginRegistry registry = new DefaultPluginRegistry();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        var descriptors = new ClasspathPluginDescriptorLoader().load(classLoader);
        new PluginDescriptorValidator().validate(descriptors);
        descriptors.forEach(descriptor -> {
            registry.register(descriptor);
            capabilityRegistry.register(descriptor.id(), descriptor.capabilities());
        });
        return registry;
    }

    @Bean
    ProviderBeanValidator providerBeanValidator(ListableBeanFactory beanFactory, Environment environment) {
        return new ProviderBeanValidator(beanFactory, environment);
    }

    @Bean
    @ConditionalOnMissingBean
    ProviderHealthCatalog providerHealthCatalog(List<ProviderHealthIndicator> indicators) {
        return new ProviderHealthCatalog(indicators);
    }
}
