package org.ssssssss.magicapi.iot.transport.mqtt.client;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.ObservableTransportProvider;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;
import org.ssssssss.magicapi.iot.core.spi.TransportSnapshot;
import org.ssssssss.magicapi.iot.core.session.SessionLifecycleCoordinator;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PahoMqttClientTransportProvider implements ObservableTransportProvider {
    private final MqttClientTransportProperties properties;
    private final DeviceRegistry registry;
    private final SessionLifecycleCoordinator sessions;
    private final Pattern lifecycleClientIdPattern;
    private final MqttClientTopicMapper topics = new MqttClientTopicMapper();
    private final Map<String, ProtocolContext> deviceContexts = new ConcurrentHashMap<>();
    private final AtomicLong acceptedConnections = new AtomicLong();
    private final AtomicLong receivedMessages = new AtomicLong();
    private final AtomicLong receivedBytes = new AtomicLong();
    private final AtomicLong sentMessages = new AtomicLong();
    private final AtomicLong sentBytes = new AtomicLong();
    private final AtomicLong errors = new AtomicLong();
    private final AtomicBoolean closing = new AtomicBoolean();
    private volatile MqttAsyncClient client;
    private volatile TransportMessageHandler handler;

    public PahoMqttClientTransportProvider(MqttClientTransportProperties properties, DeviceRegistry registry) {
        this(properties, registry, null);
    }

    public PahoMqttClientTransportProvider(MqttClientTransportProperties properties, DeviceRegistry registry,
                                           org.ssssssss.magicapi.iot.core.spi.SessionRepository sessionRepository) {
        this.properties = properties;
        this.registry = registry;
        this.sessions = sessionRepository == null ? null : new SessionLifecycleCoordinator(sessionRepository, properties.getNodeId());
        this.lifecycleClientIdPattern = Pattern.compile(properties.getLifecycleClientIdPattern());
    }

    @Override public String transportId() { return "mqtt-client"; }

    @Override
    public synchronized void start(TransportMessageHandler handler) {
        if (isRunning()) return;
        validateConfiguration();
        this.handler = handler;
        closing.set(false);
        try {
            MqttAsyncClient candidate = new MqttAsyncClient(properties.getServerUri(), properties.clientId(),
                new MemoryPersistence());
            candidate.setCallback(new Callback());
            client = candidate;
            candidate.connect(connectOptions()).waitForCompletion(properties.getConnectTimeout().toMillis());
            subscribe(candidate);
        } catch (MqttException exception) {
            errors.incrementAndGet();
            close();
            throw new IllegalStateException("Failed to connect MQTT broker " + properties.getServerUri(), exception);
        }
    }

    @Override
    public void send(String connectionId, ByteBuffer payload) {
        MqttAsyncClient current = requireConnected();
        DeviceIdentity device = identity(connectionId);
        byte[] bytes = new byte[payload.remaining()];
        payload.asReadOnlyBuffer().get(bytes);
        String topic = topics.downlink(properties.getDownlinkTopic(), device);
        try {
            current.publish(topic, bytes, properties.getDownlinkQos(), false)
                .waitForCompletion(properties.getPublishTimeout().toMillis());
            sentMessages.incrementAndGet();
            sentBytes.addAndGet(bytes.length);
        } catch (MqttException exception) {
            errors.incrementAndGet();
            throw new IllegalStateException("MQTT publish failed for device " + device.routingKey(), exception);
        }
    }

    @Override
    public void disconnect(String connectionId) {
        ProtocolContext context = deviceContexts.remove(connectionId);
        if (context != null && handler != null) handler.disconnected(connectionId, context, null);
    }

    @Override public boolean isRunning() { return client != null && client.isConnected(); }

    @Override
    public TransportSnapshot snapshot() {
        URI uri = URI.create(properties.getServerUri());
        int port = uri.getPort() >= 0 ? uri.getPort() : ("ssl".equalsIgnoreCase(uri.getScheme()) ? 8883 : 1883);
        return new TransportSnapshot(transportId(), isRunning(), properties.getServerUri(), port,
            deviceContexts.size(), acceptedConnections.get(), receivedMessages.get(), receivedBytes.get(),
            sentMessages.get(), sentBytes.get(), errors.get());
    }

    @Override
    public synchronized void close() {
        closing.set(true);
        MqttAsyncClient current = client;
        client = null;
        if (current != null) {
            try { if (current.isConnected()) current.disconnect().waitForCompletion(3000); }
            catch (MqttException ignored) { }
            try { current.close(); } catch (MqttException ignored) { }
        }
        disconnectVirtualDevices(null);
    }

    private void messageArrived(String topic, MqttMessage mqttMessage) {
        try {
            MqttClientTopicMapper.MappedTopic mapped = topics.map(topic);
            if (properties.isValidateDevice()
                && registry.find(mapped.device()).filter(RegisteredDevice::enabled).isEmpty()) {
                errors.incrementAndGet();
                return;
            }
            String connectionId = mapped.device().routingKey();
            ProtocolContext context = new ProtocolContext("mqtt", properties.getServerUri(), mapped.device(), Map.of(
                "connectionId", connectionId, "brokerClientId", properties.clientId(), "topic", topic));
            if (deviceContexts.putIfAbsent(connectionId, context) == null) {
                acceptedConnections.incrementAndGet();
                if (sessions != null) sessions.connected(connectionId, context);
                handler.connected(connectionId, context);
            }
            if (sessions != null) sessions.touch(connectionId);
            byte[] payload = mqttMessage.getPayload();
            Map<String, String> metadata = new LinkedHashMap<>();
            metadata.put("topic", topic);
            metadata.put("qos", Integer.toString(mqttMessage.getQos()));
            metadata.put("retained", Boolean.toString(mqttMessage.isRetained()));
            metadata.put("duplicate", Boolean.toString(mqttMessage.isDuplicate()));
            metadata.put("detail", mapped.detail());
            metadata.put("brokerUri", properties.getServerUri());
            DeviceMessage message = new DeviceMessage(null, mapped.device(), mapped.type(), "mqtt", Instant.now(),
                null, payload, metadata);
            receivedMessages.incrementAndGet();
            receivedBytes.addAndGet(payload.length);
            handler.received(connectionId, message, context);
        } catch (RuntimeException exception) {
            errors.incrementAndGet();
        }
    }

    private void subscribe(MqttAsyncClient current) throws MqttException {
        java.util.List<MqttClientTransportProperties.Subscription> all = new java.util.ArrayList<>(properties.getSubscriptions());
        all.addAll(properties.getLifecycleSubscriptions());
        String[] filters = all.stream().map(MqttClientTransportProperties.Subscription::getTopic)
            .toArray(String[]::new);
        int[] qos = all.stream().mapToInt(MqttClientTransportProperties.Subscription::getQos)
            .toArray();
        current.subscribe(filters, qos).waitForCompletion(properties.getConnectTimeout().toMillis());
    }

    private MqttConnectOptions connectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(properties.isAutomaticReconnect());
        options.setCleanSession(properties.isCleanSession());
        options.setKeepAliveInterval(Math.toIntExact(properties.getKeepAlive().toSeconds()));
        options.setConnectionTimeout(Math.toIntExact(properties.getConnectTimeout().toSeconds()));
        if (properties.getUsername() != null && !properties.getUsername().isBlank()) {
            options.setUserName(properties.getUsername());
        }
        if (properties.getPassword() != null && !properties.getPassword().isBlank()) {
            options.setPassword(properties.getPassword().toCharArray());
        }
        return options;
    }

    private void validateConfiguration() {
        URI.create(properties.getServerUri());
        if (properties.getSubscriptions() == null || properties.getSubscriptions().isEmpty()) {
            throw new IllegalStateException("At least one MQTT subscription is required");
        }
        for (MqttClientTransportProperties.Subscription subscription : properties.getSubscriptions()) {
            if (subscription.getTopic() == null || subscription.getTopic().isBlank()) {
                throw new IllegalStateException("MQTT subscription topic must not be blank");
            }
            if (subscription.getQos() < 0 || subscription.getQos() > 2) {
                throw new IllegalStateException("MQTT subscription QoS must be between 0 and 2");
            }
        }
        try { Pattern.compile(properties.getLifecycleClientIdPattern()); }
        catch (RuntimeException exception) { throw new IllegalStateException("Invalid MQTT lifecycle clientId pattern", exception); }
    }

    private boolean lifecycleMessageArrived(String topic, MqttMessage message) {
        String event = lifecycleEvent(topic);
        if (event == null) return false;
        String clientId = lifecycleClientId(topic);
        Matcher matcher = lifecycleClientIdPattern.matcher(clientId);
        if (!matcher.matches()) { errors.incrementAndGet(); return true; }
        String productId = matcher.group("productId");
        String deviceId = matcher.group("deviceId");
        DeviceIdentity identity = new DeviceIdentity(productId, deviceId);
        if (properties.isValidateDevice() && registry.find(identity).filter(RegisteredDevice::enabled).isEmpty()) return true;
        String connectionId = identity.routingKey();
        ProtocolContext context = new ProtocolContext("mqtt", properties.getServerUri(), identity, Map.of(
            "connectionId", connectionId, "brokerClientId", clientId, "lifecycleEvent", event,
            "topic", topic));
        if ("connected".equals(event)) {
            if (deviceContexts.putIfAbsent(connectionId, context) == null) {
                acceptedConnections.incrementAndGet();
                if (sessions != null) sessions.connected(connectionId, context);
                if (handler != null) handler.connected(connectionId, context);
            } else if (sessions != null) {
                sessions.touch(connectionId);
            }
        } else {
            ProtocolContext previous = deviceContexts.remove(connectionId);
            if (sessions != null) sessions.disconnected(connectionId);
            if (handler != null && previous != null) handler.disconnected(connectionId, previous, null);
        }
        return true;
    }

    private static String lifecycleEvent(String topic) {
        if (topic.endsWith("/connected")) return "connected";
        if (topic.endsWith("/disconnected")) return "disconnected";
        return null;
    }

    private static String lifecycleClientId(String topic) {
        String[] parts = topic.split("/");
        return parts.length == 0 ? "" : parts[parts.length - 2];
    }

    private DeviceIdentity identity(String connectionId) {
        ProtocolContext context = deviceContexts.get(connectionId);
        if (context != null) return context.device();
        String[] parts = connectionId == null ? new String[0] : connectionId.split("/", 2);
        if (parts.length != 2) throw new IllegalArgumentException("MQTT device connectionId must be productId/deviceId");
        return new DeviceIdentity(parts[0], parts[1]);
    }

    private MqttAsyncClient requireConnected() {
        MqttAsyncClient current = client;
        if (current == null || !current.isConnected()) throw new IllegalStateException("MQTT client transport is not connected");
        return current;
    }

    private void disconnectVirtualDevices(Throwable cause) {
        TransportMessageHandler currentHandler = handler;
        if (currentHandler != null) {
            deviceContexts.forEach((id, context) -> currentHandler.disconnected(id, context, cause));
        }
        deviceContexts.clear();
    }

    private final class Callback implements MqttCallbackExtended {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            if (reconnect && client != null) {
                try { subscribe(client); } catch (MqttException exception) { errors.incrementAndGet(); }
            }
        }

        @Override public void connectionLost(Throwable cause) {
            if (!closing.get()) {
                errors.incrementAndGet();
                disconnectVirtualDevices(cause);
            }
        }
        @Override public void messageArrived(String topic, MqttMessage message) {
            if (!lifecycleMessageArrived(topic, message)) PahoMqttClientTransportProvider.this.messageArrived(topic, message);
        }
        @Override public void deliveryComplete(IMqttDeliveryToken token) { }
    }
}
