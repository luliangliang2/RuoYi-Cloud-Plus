package org.ssssssss.magicapi.iot.cluster.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@AutoConfiguration
@EnableConfigurationProperties(ZookeeperNodeRegistryProperties.class)
@ConditionalOnProperty(prefix = "iot.providers.node-registry", name = "type", havingValue = "zookeeper")
public class ZookeeperNodeRegistryAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(NodeRegistry.class)
    ZookeeperNodeRegistry zookeeperNodeRegistry(ZookeeperNodeRegistryProperties properties, ObjectMapper mapper)
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
        return new ZookeeperNodeRegistry(client, mapper, properties);
    }

    @Bean
    ProviderHealthIndicator zookeeperNodeRegistryHealth(ZookeeperNodeRegistry registry,
                                                         ZookeeperNodeRegistryProperties properties) {
        return new ProbeProviderHealthIndicator("node-registry", "zookeeper", Duration.ofSeconds(5), Duration.ofSeconds(3),
            () -> registry.isAvailable()
                ? ProbeProviderHealthIndicator.up(Map.of("connectString", properties.getConnectString()))
                : new PluginHealth(PluginHealth.Status.DOWN, "ZooKeeper is disconnected", Map.of()));
    }
}
