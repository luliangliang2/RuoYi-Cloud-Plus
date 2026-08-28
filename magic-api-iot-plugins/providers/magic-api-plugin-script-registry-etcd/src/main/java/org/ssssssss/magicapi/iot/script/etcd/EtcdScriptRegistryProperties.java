package org.ssssssss.magicapi.iot.script.etcd;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("iot.script.registry.etcd")
public class EtcdScriptRegistryProperties {
    private List<String> endpoints = new ArrayList<>(List.of("http://127.0.0.1:2379"));
    private String username;
    private String password;
    private String rootPrefix = "/iot/gateway/scripts/";
    private Duration requestTimeout = Duration.ofSeconds(5);

    public List<String> getEndpoints() { return endpoints; }
    public void setEndpoints(List<String> endpoints) { this.endpoints = endpoints; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRootPrefix() { return rootPrefix; }
    public void setRootPrefix(String rootPrefix) { this.rootPrefix = rootPrefix; }
    public Duration getRequestTimeout() { return requestTimeout; }
    public void setRequestTimeout(Duration requestTimeout) { this.requestTimeout = requestTimeout; }

    void validate() {
        if (endpoints == null || endpoints.isEmpty()) throw new IllegalStateException("iot.script.registry.etcd.endpoints must not be empty");
        for (String endpoint : endpoints) {
            URI uri = URI.create(endpoint);
            if (uri.getScheme() == null || uri.getHost() == null) throw new IllegalStateException("Invalid etcd script registry endpoint: " + endpoint);
        }
        if (rootPrefix == null || !rootPrefix.startsWith("/") || !rootPrefix.endsWith("/")) throw new IllegalStateException("iot.script.registry.etcd.root-prefix must start and end with '/'");
        if (requestTimeout == null || requestTimeout.isZero() || requestTimeout.isNegative()) throw new IllegalStateException("iot.script.registry.etcd.request-timeout must be positive");
        boolean hasUsername = username != null && !username.isBlank();
        boolean hasPassword = password != null && !password.isBlank();
        if (hasUsername != hasPassword) throw new IllegalStateException("iot.script.registry.etcd.username and password must be configured together");
    }
}
