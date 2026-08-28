package org.ssssssss.magicapi.iot.cluster.etcd;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("iot.node-registry.etcd")
public class EtcdNodeRegistryProperties {
    private List<String> endpoints = new ArrayList<>(List.of("http://127.0.0.1:2379"));
    private String username;
    private String password;
    private String rootPrefix = "/iot/gateway/nodes/";
    private Duration leaseTtl = Duration.ofSeconds(20);
    private Duration requestTimeout = Duration.ofSeconds(5);
    private Duration staleAfter = Duration.ofSeconds(20);

    public List<String> getEndpoints() { return endpoints; }
    public void setEndpoints(List<String> endpoints) { this.endpoints = endpoints; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRootPrefix() { return rootPrefix; }
    public void setRootPrefix(String rootPrefix) { this.rootPrefix = rootPrefix; }
    public Duration getLeaseTtl() { return leaseTtl; }
    public void setLeaseTtl(Duration leaseTtl) { this.leaseTtl = leaseTtl; }
    public Duration getRequestTimeout() { return requestTimeout; }
    public void setRequestTimeout(Duration requestTimeout) { this.requestTimeout = requestTimeout; }
    public Duration getStaleAfter() { return staleAfter; }
    public void setStaleAfter(Duration staleAfter) { this.staleAfter = staleAfter; }

    void validate() {
        if (endpoints == null || endpoints.isEmpty())
            throw new IllegalStateException("iot.node-registry.etcd.endpoints must not be empty");
        for (String endpoint : endpoints) {
            try {
                URI uri = URI.create(endpoint);
                if (uri.getScheme() == null || uri.getHost() == null)
                    throw new IllegalArgumentException();
            } catch (IllegalArgumentException exception) {
                throw new IllegalStateException("Invalid etcd endpoint: " + endpoint, exception);
            }
        }
        if (rootPrefix == null || !rootPrefix.startsWith("/") || !rootPrefix.endsWith("/"))
            throw new IllegalStateException("iot.node-registry.etcd.root-prefix must start and end with '/'");
        requirePositive(leaseTtl, "iot.node-registry.etcd.lease-ttl");
        requirePositive(requestTimeout, "iot.node-registry.etcd.request-timeout");
        requirePositive(staleAfter, "iot.node-registry.etcd.stale-after");
        boolean hasUsername = username != null && !username.isBlank();
        boolean hasPassword = password != null && !password.isBlank();
        if (hasUsername != hasPassword)
            throw new IllegalStateException("iot.node-registry.etcd.username and password must be configured together");
    }

    private static void requirePositive(Duration value, String property) {
        if (value == null || value.isZero() || value.isNegative())
            throw new IllegalStateException(property + " must be positive");
    }
}
