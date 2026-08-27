package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties("iot.handshake")
public class HandshakeProperties {
    private boolean enabled;
    private Duration timeout = Duration.ofSeconds(10);
    private int maxAttempts = 3;
    private String gatewayNodeId = "local";
    private Map<String, String> providers = new HashMap<>();
    private Set<String> requiredProtocols = new HashSet<>();

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Duration getTimeout() { return timeout; }
    public void setTimeout(Duration timeout) { this.timeout = timeout; }
    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public String getGatewayNodeId() { return gatewayNodeId; }
    public void setGatewayNodeId(String gatewayNodeId) { this.gatewayNodeId = gatewayNodeId; }
    public Map<String, String> getProviders() { return providers; }
    public void setProviders(Map<String, String> providers) { this.providers = providers; }
    public Set<String> getRequiredProtocols() { return requiredProtocols; }
    public void setRequiredProtocols(Set<String> requiredProtocols) { this.requiredProtocols = requiredProtocols; }
}
