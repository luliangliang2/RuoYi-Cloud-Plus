package org.ssssssss.magicapi.iot.config.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

@AutoConfiguration
@EnableConfigurationProperties(NacosConfigurationCenterProperties.class)
@ConditionalOnProperty(prefix = "iot.providers.configuration-center", name = "type", havingValue = "nacos")
public class NacosConfigurationCenterAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(ConfigurationCenter.class)
    NacosConfigurationCenter nacosConfigurationCenter(NacosConfigurationCenterProperties properties,
                                                       ObjectMapper mapper) throws Exception {
        properties.validate();
        Properties client = new Properties();
        client.setProperty(PropertyKeyConst.SERVER_ADDR, properties.getServerAddr());
        if (properties.getNamespace() != null) client.setProperty(PropertyKeyConst.NAMESPACE, properties.getNamespace());
        if (properties.getUsername() != null) client.setProperty(PropertyKeyConst.USERNAME, properties.getUsername());
        if (properties.getPassword() != null) client.setProperty(PropertyKeyConst.PASSWORD, properties.getPassword());
        ConfigService configService = NacosFactory.createConfigService(client);
        return new NacosConfigurationCenter(configService, mapper, properties);
    }

    @Bean
    ProviderHealthIndicator nacosConfigurationCenterHealth(NacosConfigurationCenter center,
                                                            NacosConfigurationCenterProperties properties) {
        return new ProbeProviderHealthIndicator("configuration-center", "nacos", Duration.ofSeconds(5), Duration.ofSeconds(3),
            () -> center.isAvailable()
                ? ProbeProviderHealthIndicator.up(Map.of("dataId", properties.getDataId(), "group", properties.getGroup()))
                : new PluginHealth(PluginHealth.Status.DOWN, "Nacos config service is unavailable", Map.of()));
    }
}
