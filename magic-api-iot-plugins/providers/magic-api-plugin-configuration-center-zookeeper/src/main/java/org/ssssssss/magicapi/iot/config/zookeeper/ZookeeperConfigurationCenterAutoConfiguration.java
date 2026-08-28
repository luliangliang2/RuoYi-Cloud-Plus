package org.ssssssss.magicapi.iot.config.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
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
import java.util.concurrent.TimeUnit;

@AutoConfiguration
@EnableConfigurationProperties(ZookeeperConfigurationCenterProperties.class)
@ConditionalOnProperty(prefix = "iot.providers.configuration-center", name = "type", havingValue = "zookeeper")
public class ZookeeperConfigurationCenterAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(ConfigurationCenter.class)
    ZookeeperConfigurationCenter zookeeperConfigurationCenter(ZookeeperConfigurationCenterProperties properties)
        throws InterruptedException {
        properties.validate();
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(properties.getConnectString())
            .sessionTimeoutMs(Math.toIntExact(properties.getSessionTimeout().toMillis()))
            .connectionTimeoutMs(Math.toIntExact(properties.getConnectionTimeout().toMillis()))
            .retryPolicy(new ExponentialBackoffRetry(properties.getRetryBaseSleepMs(), properties.getMaxRetries()))
            .build();
        client.start();
        if (!client.blockUntilConnected(Math.toIntExact(properties.getConnectionTimeout().toMillis()), TimeUnit.MILLISECONDS)) {
            client.close();
            throw new IllegalStateException("Timed out connecting to ZooKeeper " + properties.getConnectString());
        }
        return new ZookeeperConfigurationCenter(client, properties);
    }

    @Bean
    ProviderHealthIndicator zookeeperConfigurationCenterHealth(ZookeeperConfigurationCenter center,
                                                                 ZookeeperConfigurationCenterProperties properties) {
        return new ProbeProviderHealthIndicator("configuration-center", "zookeeper", Duration.ofSeconds(5), Duration.ofSeconds(3),
            () -> center.isAvailable()
                ? ProbeProviderHealthIndicator.up(Map.of("connectString", properties.getConnectString(), "rootPath", properties.getRootPath()))
                : new PluginHealth(PluginHealth.Status.DOWN, "ZooKeeper configuration center is disconnected", Map.of()));
    }
}
