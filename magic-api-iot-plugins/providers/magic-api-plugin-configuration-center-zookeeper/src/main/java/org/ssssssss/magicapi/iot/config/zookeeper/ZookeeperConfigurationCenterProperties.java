package org.ssssssss.magicapi.iot.config.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("iot.configuration-center.zookeeper")
public class ZookeeperConfigurationCenterProperties {
    private String connectString = "127.0.0.1:2181";
    private String rootPath = "/iot/gateway/config";
    private Duration sessionTimeout = Duration.ofSeconds(20);
    private Duration connectionTimeout = Duration.ofSeconds(5);
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
    public int getRetryBaseSleepMs() { return retryBaseSleepMs; }
    public void setRetryBaseSleepMs(int retryBaseSleepMs) { this.retryBaseSleepMs = retryBaseSleepMs; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }

    void validate() {
        if (connectString == null || connectString.isBlank())
            throw new IllegalStateException("iot.configuration-center.zookeeper.connect-string must not be blank");
        if (rootPath == null || !rootPath.startsWith("/") || rootPath.endsWith("/"))
            throw new IllegalStateException("iot.configuration-center.zookeeper.root-path must start with '/' and not end with '/'");
        requirePositive(sessionTimeout, "session-timeout");
        requirePositive(connectionTimeout, "connection-timeout");
        if (retryBaseSleepMs <= 0)
            throw new IllegalStateException("iot.configuration-center.zookeeper.retry-base-sleep-ms must be positive");
        if (maxRetries < 0)
            throw new IllegalStateException("iot.configuration-center.zookeeper.max-retries must not be negative");
    }

    private static void requirePositive(Duration duration, String name) {
        if (duration == null || duration.isZero() || duration.isNegative())
            throw new IllegalStateException("iot.configuration-center.zookeeper." + name + " must be positive");
    }
}
