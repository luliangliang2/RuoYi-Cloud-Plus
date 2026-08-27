package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.env.Environment;
import org.ssssssss.magicapi.iot.plugin.runtime.CapabilityRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.ClasspathPluginDescriptorLoader;
import org.ssssssss.magicapi.iot.plugin.runtime.DefaultCapabilityRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.DefaultPluginRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginDescriptorValidator;
import org.ssssssss.magicapi.iot.plugin.runtime.DefaultPluginServiceRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.ExternalPluginManager;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginServiceRegistry;
import org.ssssssss.magicapi.iot.plugin.api.PluginService;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.HandshakeProviderRegistry;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;
import org.ssssssss.magicapi.iot.config.ConfigurationRuntime;
import org.ssssssss.magicapi.iot.config.InMemoryConfigurationRuntime;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties({ExternalPluginProperties.class, HandshakeProperties.class})
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
    @ConditionalOnMissingBean
    PluginServiceRegistry pluginServiceRegistry() {
        return new DefaultPluginServiceRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    HandshakeProviderRegistry handshakeProviderRegistry(PluginServiceRegistry services) {
        return new DefaultHandshakeProviderRegistry(services);
    }

    @Bean
    HandshakeDebugService handshakeDebugService(PluginServiceRegistry services) {
        return new HandshakeDebugService(services);
    }

    @Bean
    @ConditionalOnMissingBean
    org.ssssssss.magicapi.iot.core.spi.HandshakeCoordinator handshakeCoordinator(
        HandshakeProperties properties, PluginServiceRegistry services, SessionRepository sessions) {
        return new DefaultHandshakeCoordinator(properties, services, sessions);
    }

    @Bean
    ClasspathPluginServiceRegistrar classpathPluginServiceRegistrar(PluginServiceRegistry registry,
                                                                    List<PluginService> services) {
        return new ClasspathPluginServiceRegistrar(registry, services);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnProperty(prefix = "iot.plugins.external", name = "enabled", havingValue = "true", matchIfMissing = true)
    ExternalPluginManager externalPluginManager(ExternalPluginProperties properties,
                                                PluginRegistry pluginRegistry,
                                                CapabilityRegistry capabilityRegistry,
                                                PluginServiceRegistry serviceRegistry,
                                                DeviceRegistry deviceRegistry,
                                                SessionRepository sessionRepository,
                                                DeviceMessageBus messageBus,
                                                ConfigurationRuntime configurationRuntime) {
        Map<Class<?>, Object> services = Map.of(
            DeviceRegistry.class, deviceRegistry,
            SessionRepository.class, sessionRepository,
            DeviceMessageBus.class, messageBus,
            ConfigurationRuntime.class, configurationRuntime);
        return new ExternalPluginManager(properties.getDirectory(), properties.getDataDirectory(), pluginRegistry,
            capabilityRegistry, serviceRegistry, Thread.currentThread().getContextClassLoader(), services);
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

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    ConfigurationRuntime configurationRuntime(ConfigurationCenter configurationCenter) {
        return new InMemoryConfigurationRuntime(configurationCenter);
    }

    @Bean
    List<JsonNamespaceConfigurationParser> configurationNamespaceParsers(ConfigurationRuntime runtime,
                                                                           ObjectMapper mapper) {
        List<JsonNamespaceConfigurationParser> parsers = List.of(
            new JsonNamespaceConfigurationParser("providers", "providers/", mapper),
            new JsonNamespaceConfigurationParser("transports", "transports/", mapper),
            new JsonNamespaceConfigurationParser("protocols", "protocols/", mapper));
        parsers.forEach(runtime::registerParser);
        return parsers;
    }

    @Bean(name = "iotProvidersHealthIndicator")
    @ConditionalOnMissingBean(name = "iotProvidersHealthIndicator")
    @org.springframework.boot.autoconfigure.condition.ConditionalOnClass(
        name = "org.springframework.boot.actuate.health.HealthIndicator")
    IotProvidersHealthIndicator iotProvidersHealthIndicator(ProviderHealthCatalog catalog) {
        return new IotProvidersHealthIndicator(catalog);
    }
}
