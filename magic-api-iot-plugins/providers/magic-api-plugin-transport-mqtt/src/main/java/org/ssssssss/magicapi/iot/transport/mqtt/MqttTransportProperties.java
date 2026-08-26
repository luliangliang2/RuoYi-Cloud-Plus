package org.ssssssss.magicapi.iot.transport.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("iot.transports.mqtt")
public class MqttTransportProperties {
    private boolean enabled;
    private String host = "0.0.0.0";
    private int port = 1883;
    private boolean authenticationRequired;
    private String credentialType = "secret";
    private String downlinkTopic = "devices/{productId}/{deviceId}/commands";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public boolean isAuthenticationRequired() { return authenticationRequired; }
    public void setAuthenticationRequired(boolean authenticationRequired) { this.authenticationRequired = authenticationRequired; }
    public String getCredentialType() { return credentialType; }
    public void setCredentialType(String credentialType) { this.credentialType = credentialType; }
    public String getDownlinkTopic() { return downlinkTopic; }
    public void setDownlinkTopic(String downlinkTopic) { this.downlinkTopic = downlinkTopic; }
}
