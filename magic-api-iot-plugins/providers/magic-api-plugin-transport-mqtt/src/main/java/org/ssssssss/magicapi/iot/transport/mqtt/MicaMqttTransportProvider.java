package org.ssssssss.magicapi.iot.transport.mqtt;

import org.dromara.mica.mqtt.codec.MqttQoS;
import org.dromara.mica.mqtt.codec.message.MqttPublishMessage;
import org.dromara.mica.mqtt.core.server.MqttServer;
import org.dromara.mica.mqtt.core.server.MqttServerCreator;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class MicaMqttTransportProvider implements ObservableTransportProvider {
    private final MqttTransportProperties properties;
    private final DeviceRegistry registry;
    private final MqttTopicMapper topics = new MqttTopicMapper();
    private final Map<String, ProtocolContext> contexts = new ConcurrentHashMap<>();
    private final AtomicLong acceptedConnections = new AtomicLong();
    private final AtomicLong receivedMessages = new AtomicLong();
    private final AtomicLong receivedBytes = new AtomicLong();
    private final AtomicLong sentMessages = new AtomicLong();
    private final AtomicLong sentBytes = new AtomicLong();
    private final AtomicLong errors = new AtomicLong();
    private volatile MqttServer server;
    private volatile TransportMessageHandler handler;

    public MicaMqttTransportProvider(MqttTransportProperties properties, DeviceRegistry registry) {
        this.properties = properties;
        this.registry = registry;
    }

    @Override public String transportId() { return "mqtt"; }

    @Override
    public synchronized void start(TransportMessageHandler handler) {
        if (isRunning()) return;
        this.handler = handler;
        MqttServerCreator creator = MqttServer.create()
            .name("iot-mqtt-transport")
            .enableMqtt(builder -> builder.serverNode(properties.getHost(), properties.getPort()).build())
            .messageListener(this::messageReceived)
            .connectStatusListener(new org.dromara.mica.mqtt.core.server.event.IMqttConnectStatusListener() {
                @Override public void online(net.dreamlu.mica.net.core.ChannelContext context, String clientId, String username) {
                    clientConnected(clientId, username);
                }
                @Override public void offline(net.dreamlu.mica.net.core.ChannelContext context, String clientId,
                                              String username, String reason) {
                    clientDisconnected(clientId, reason);
                }
            });
        if (properties.isAuthenticationRequired()) creator.authHandler(this::authenticate);
        MqttServer candidate = creator.build();
        if (!candidate.start()) throw new IllegalStateException("MQTT broker failed to start on port " + properties.getPort());
        server = candidate;
    }

    @Override
    public void send(String connectionId, ByteBuffer payload) {
        MqttServer current = requireRunning();
        ProtocolContext context = contexts.get(connectionId);
        if (context == null) throw new IllegalArgumentException("Unknown MQTT connection: " + connectionId);
        byte[] bytes = new byte[payload.remaining()];
        payload.asReadOnlyBuffer().get(bytes);
        String topic = properties.getDownlinkTopic()
            .replace("{productId}", context.device().productId())
            .replace("{deviceId}", context.device().deviceId());
        if (!current.publish(connectionId, topic, bytes, MqttQoS.QOS1)) {
            errors.incrementAndGet();
            throw new IllegalStateException("MQTT publish failed for client " + connectionId);
        }
        sentMessages.incrementAndGet();
        sentBytes.addAndGet(bytes.length);
    }

    @Override public void disconnect(String connectionId) { requireRunning().disconnect(connectionId); }
    @Override public boolean isRunning() { return server != null; }

    @Override
    public TransportSnapshot snapshot() {
        return new TransportSnapshot(transportId(), isRunning(), properties.getHost(), properties.getPort(),
            contexts.size(), acceptedConnections.get(), receivedMessages.get(), receivedBytes.get(),
            sentMessages.get(), sentBytes.get(), errors.get());
    }

    @Override
    public synchronized void close() {
        MqttServer current = server;
        server = null;
        if (current != null) current.stop();
        contexts.clear();
    }

    private boolean authenticate(net.dreamlu.mica.net.core.ChannelContext context, String clientId,
                                 String username, String password, String address) {
        var identity = topics.identity(username == null || username.isBlank() ? clientId : username);
        return registry.find(identity).filter(RegisteredDevice::enabled).isPresent()
            && password != null
            && registry.authenticate(identity, new DeviceCredential(properties.getCredentialType(), password));
    }

    private void clientConnected(String clientId, String username) {
        var identity = topics.identity(username == null || username.isBlank() ? clientId : username);
        ProtocolContext context = new ProtocolContext("mqtt", "mqtt-client:" + clientId, identity,
            Map.of("connectionId", clientId, "clientId", clientId));
        contexts.put(clientId, context);
        acceptedConnections.incrementAndGet();
        handler.connected(clientId, context);
    }

    private void clientDisconnected(String clientId, String reason) {
        ProtocolContext context = contexts.remove(clientId);
        handler.disconnected(clientId, context, null);
    }

    private void messageReceived(net.dreamlu.mica.net.core.ChannelContext channel, String clientId, String topic,
                                 MqttQoS qos, MqttPublishMessage publish) {
        try {
            byte[] payload = publish.getPayload();
            MqttTopicMapper.MappedTopic mapped = topics.map(topic, clientId);
            ProtocolContext base = contexts.get(clientId);
            ProtocolContext context = new ProtocolContext("mqtt", base == null ? "mqtt-client:" + clientId : base.remoteAddress(),
                mapped.device(), Map.of("connectionId", clientId, "clientId", clientId, "topic", topic));
            Map<String, String> metadata = Map.of(
                "topic", topic,
                "qos", Integer.toString(qos.value()),
                "retained", Boolean.toString(publish.fixedHeader().isRetain()),
                "duplicate", Boolean.toString(publish.fixedHeader().isDup()),
                "detail", mapped.detail());
            DeviceMessage message = new DeviceMessage(null, mapped.device(), mapped.type(), "mqtt", Instant.now(),
                null, payload, metadata);
            receivedMessages.incrementAndGet();
            receivedBytes.addAndGet(payload.length);
            handler.received(clientId, message, context);
        } catch (RuntimeException exception) {
            errors.incrementAndGet();
            throw exception;
        }
    }

    private MqttServer requireRunning() {
        MqttServer current = server;
        if (current == null) throw new IllegalStateException("MQTT transport is not running");
        return current;
    }
}
