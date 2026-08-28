package org.ssssssss.magicapi.iot.cluster.nacos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("iot.node-registry.nacos")
public class NacosNodeRegistryProperties {
    private String serverAddr = "127.0.0.1:8848";
    private String namespace = "public";
    private String username = "nacos";
    private String password = "nacos";
    private String serviceName = "iot-gateway-nodes";
    private String group = "DEFAULT_GROUP";
    private String clusterName = "DEFAULT";
    private Duration staleAfter = Duration.ofSeconds(20);

    public String getServerAddr() { return serverAddr; }
    public void setServerAddr(String serverAddr) { this.serverAddr = serverAddr; }
    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) { this.namespace = namespace; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    public String getClusterName() { return clusterName; }
    public void setClusterName(String clusterName) { this.clusterName = clusterName; }
    public Duration getStaleAfter() { return staleAfter; }
    public void setStaleAfter(Duration staleAfter) { this.staleAfter = staleAfter; }

    void validate() {
        requireText(serverAddr, "iot.node-registry.nacos.server-addr");
        requireText(serviceName, "iot.node-registry.nacos.service-name");
        requireText(group, "iot.node-registry.nacos.group");
        requireText(clusterName, "iot.node-registry.nacos.cluster-name");
        requirePositive(staleAfter, "iot.node-registry.nacos.stale-after");
    }

    private static void requireText(String value, String property) {
        if (value == null || value.isBlank()) throw new IllegalStateException(property + " must not be blank");
    }

    private static void requirePositive(Duration value, String property) {
        if (value == null || value.isZero() || value.isNegative())
            throw new IllegalStateException(property + " must be positive");
    }
}
