package org.ssssssss.magicapi.iot.cluster.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("iot.node-registry.zookeeper")
public class ZookeeperNodeRegistryProperties {
    private String connectString = "127.0.0.1:2181";
    private String rootPath = "/iot/gateway/nodes";
    private Duration sessionTimeout = Duration.ofSeconds(20);
    private Duration connectionTimeout = Duration.ofSeconds(5);
    private Duration staleAfter = Duration.ofSeconds(20);
    private int retryBaseSleepMs = 500;
    private int maxRetries = 3;

    public String getConnectString() { return connectString; }
    public void setConnectString(String connectString) { this.connectString = connectString; }
    public String getRootPath() { return rootPath; }
    public void setRootPath(String rootPath) { this.rootPath = rootPath; }
    public Duration getSessionTimeout() { return sessionTimeout; }
    public void setSessionTimeout(Duration sessionTimeout) { this.sessionTimeout = sessionTimeout; }
    public Duration getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(Duration connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    public Duration getStaleAfter() { return staleAfter; }
    public void setStaleAfter(Duration staleAfter) { this.staleAfter = staleAfter; }
    public int getRetryBaseSleepMs() { return retryBaseSleepMs; }
    public void setRetryBaseSleepMs(int retryBaseSleepMs) { this.retryBaseSleepMs = retryBaseSleepMs; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }

    void validate() {
        requireText(connectString, "iot.node-registry.zookeeper.connect-string");
        if (rootPath == null || !rootPath.startsWith("/") || rootPath.endsWith("/"))
            throw new IllegalStateException("iot.node-registry.zookeeper.root-path must start with '/' and not end with '/'");
        requirePositive(sessionTimeout, "iot.node-registry.zookeeper.session-timeout");
        requirePositive(connectionTimeout, "iot.node-registry.zookeeper.connection-timeout");
        requirePositive(staleAfter, "iot.node-registry.zookeeper.stale-after");
        if (retryBaseSleepMs <= 0) throw new IllegalStateException("iot.node-registry.zookeeper.retry-base-sleep-ms must be positive");
        if (maxRetries < 0) throw new IllegalStateException("iot.node-registry.zookeeper.max-retries must not be negative");
    }

    private static void requireText(String value, String property) {
        if (value == null || value.isBlank()) throw new IllegalStateException(property + " must not be blank");
    }

    private static void requirePositive(Duration value, String property) {
        if (value == null || value.isZero() || value.isNegative())
            throw new IllegalStateException(property + " must be positive");
    }
}
