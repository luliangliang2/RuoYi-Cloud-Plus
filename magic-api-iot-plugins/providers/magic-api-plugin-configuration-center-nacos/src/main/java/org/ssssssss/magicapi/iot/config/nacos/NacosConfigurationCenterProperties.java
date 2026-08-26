package org.ssssssss.magicapi.iot.config.nacos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("iot.configuration-center.nacos")
public class NacosConfigurationCenterProperties {
    private String serverAddr = "127.0.0.1:8848";
    private String namespace = "public";
    private String username = "nacos";
    private String password = "nacos";
    private String dataId = "iot-gateway-config.json";
    private String group = "DEFAULT_GROUP";
    private Duration timeout = Duration.ofSeconds(5);
    private int maxCasRetries = 5;

    public String getServerAddr() { return serverAddr; }
    public void setServerAddr(String serverAddr) { this.serverAddr = serverAddr; }
    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) { this.namespace = namespace; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDataId() { return dataId; }
    public void setDataId(String dataId) { this.dataId = dataId; }
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    public Duration getTimeout() { return timeout; }
    public void setTimeout(Duration timeout) { this.timeout = timeout; }
    public int getMaxCasRetries() { return maxCasRetries; }
    public void setMaxCasRetries(int maxCasRetries) { this.maxCasRetries = maxCasRetries; }

    void validate() {
        requireText(serverAddr, "server-addr");
        requireText(dataId, "data-id");
        requireText(group, "group");
        if (timeout == null || timeout.isZero() || timeout.isNegative())
            throw new IllegalStateException("iot.configuration-center.nacos.timeout must be positive");
        if (maxCasRetries <= 0)
            throw new IllegalStateException("iot.configuration-center.nacos.max-cas-retries must be positive");
    }

    private static void requireText(String value, String name) {
        if (value == null || value.isBlank())
            throw new IllegalStateException("iot.configuration-center.nacos." + name + " must not be blank");
    }
}
