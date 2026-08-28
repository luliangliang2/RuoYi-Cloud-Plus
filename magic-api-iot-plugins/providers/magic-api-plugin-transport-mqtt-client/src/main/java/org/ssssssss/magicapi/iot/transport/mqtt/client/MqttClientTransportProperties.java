package org.ssssssss.magicapi.iot.transport.mqtt.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("iot.transports.mqtt-client")
public class MqttClientTransportProperties {
    private boolean enabled;
    private String serverUri = "tcp://127.0.0.1:1883";
    private String clientIdPrefix = "iot-gateway";
    private String nodeId = "node-1";
    private String username;
    private String password;
    private boolean automaticReconnect = true;
    private boolean cleanSession = true;
    private Duration keepAlive = Duration.ofSeconds(30);
    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration publishTimeout = Duration.ofSeconds(10);
    private boolean validateDevice = true;
    private String downlinkTopic = "devices/{productId}/{deviceId}/commands";
    private int downlinkQos = 1;
    private String lifecycleClientIdPattern = "(?<productId>[^/]+)/(?<deviceId>[^/]+)";
    private List<Subscription> lifecycleSubscriptions = new ArrayList<>(List.of(
        new Subscription("$share/iot-gateway/$SYS/brokers/+/clients/+/connected", 1),
        new Subscription("$share/iot-gateway/$SYS/brokers/+/clients/+/disconnected", 1)
    ));
    private List<Subscription> subscriptions = new ArrayList<>(List.of(
        new Subscription("$share/iot-gateway/devices/+/+/properties", 1),
        new Subscription("$share/iot-gateway/devices/+/+/events/+", 1),
        new Subscription("$share/iot-gateway/devices/+/+/commands/reply", 1),
        new Subscription("$share/iot-gateway/devices/+/+/heartbeat", 1)
    ));

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getServerUri() { return serverUri; }
    public void setServerUri(String serverUri) { this.serverUri = serverUri; }
    public String getClientIdPrefix() { return clientIdPrefix; }
    public void setClientIdPrefix(String clientIdPrefix) { this.clientIdPrefix = clientIdPrefix; }
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isAutomaticReconnect() { return automaticReconnect; }
    public void setAutomaticReconnect(boolean automaticReconnect) { this.automaticReconnect = automaticReconnect; }
    public boolean isCleanSession() { return cleanSession; }
    public void setCleanSession(boolean cleanSession) { this.cleanSession = cleanSession; }
    public Duration getKeepAlive() { return keepAlive; }
    public void setKeepAlive(Duration keepAlive) { this.keepAlive = keepAlive; }
    public Duration getConnectTimeout() { return connectTimeout; }
    public void setConnectTimeout(Duration connectTimeout) { this.connectTimeout = connectTimeout; }
    public Duration getPublishTimeout() { return publishTimeout; }
    public void setPublishTimeout(Duration publishTimeout) { this.publishTimeout = publishTimeout; }
    public boolean isValidateDevice() { return validateDevice; }
    public void setValidateDevice(boolean validateDevice) { this.validateDevice = validateDevice; }
    public String getDownlinkTopic() { return downlinkTopic; }
    public void setDownlinkTopic(String downlinkTopic) { this.downlinkTopic = downlinkTopic; }
    public int getDownlinkQos() { return downlinkQos; }
    public void setDownlinkQos(int downlinkQos) { this.downlinkQos = downlinkQos; }
    public String getLifecycleClientIdPattern() { return lifecycleClientIdPattern; }
    public void setLifecycleClientIdPattern(String pattern) { this.lifecycleClientIdPattern = pattern; }
    public List<Subscription> getLifecycleSubscriptions() { return lifecycleSubscriptions; }
    public void setLifecycleSubscriptions(List<Subscription> subscriptions) { this.lifecycleSubscriptions = subscriptions; }
    public List<Subscription> getSubscriptions() { return subscriptions; }
    public void setSubscriptions(List<Subscription> subscriptions) { this.subscriptions = subscriptions; }

    public String clientId() {
        return sanitize(clientIdPrefix) + "-" + sanitize(nodeId);
    }

    private static String sanitize(String value) {
        String candidate = value == null ? "" : value.replaceAll("[^a-zA-Z0-9._-]", "-");
        return candidate.isBlank() ? "unknown" : candidate;
    }

    public static class Subscription {
        private String topic;
        private int qos = 1;

        public Subscription() { }
        public Subscription(String topic, int qos) { this.topic = topic; this.qos = qos; }
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public int getQos() { return qos; }
        public void setQos(int qos) { this.qos = qos; }
    }
}
