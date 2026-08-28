package org.ssssssss.magicapi.iot.transport.mqtt.client;

import org.dromara.mica.mqtt.core.server.MqttServer;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class PahoMqttClientTransportProviderTest {
    @Test
    void consumesDeviceMessageAndPublishesCommandThroughBroker() throws Exception {
        int port = availablePort();
        var broker = MqttServer.create().name("mqtt-client-provider-test")
            .enableMqtt(builder -> builder.serverNode("127.0.0.1", port).build()).build();
        assertTrue(broker.start());

        DeviceIdentity identity = new DeviceIdentity("factory", "robot-01");
        DeviceRegistry registry = registry(new RegisteredDevice(identity, true, "mqtt", Map.of(), 1));
        MqttClientTransportProperties properties = new MqttClientTransportProperties();
        properties.setServerUri("tcp://127.0.0.1:" + port);
        properties.setNodeId("test-node");
        properties.setConnectTimeout(Duration.ofSeconds(5));
        properties.setPublishTimeout(Duration.ofSeconds(5));
        properties.setSubscriptions(java.util.List.of(
            new MqttClientTransportProperties.Subscription("devices/+/+/properties", 1)));

        PahoMqttClientTransportProvider transport = new PahoMqttClientTransportProvider(properties, registry);
        CountDownLatch received = new CountDownLatch(1);
        AtomicReference<DeviceMessage> inbound = new AtomicReference<>();
        MqttClient robot = new MqttClient(properties.getServerUri(), "factory/robot-01", new MemoryPersistence());
        CountDownLatch commandReceived = new CountDownLatch(1);
        AtomicReference<byte[]> command = new AtomicReference<>();
        try {
            transport.start(handler(inbound, received));
            robot.setCallback(new MqttCallback() {
                @Override public void connectionLost(Throwable cause) { }
                @Override public void messageArrived(String topic, MqttMessage message) {
                    command.set(message.getPayload());
                    commandReceived.countDown();
                }
                @Override public void deliveryComplete(IMqttDeliveryToken token) { }
            });
            robot.connect();
            robot.subscribe("devices/factory/robot-01/commands", 1);
            robot.publish("devices/factory/robot-01/properties",
                "{\"speed\":1.2}".getBytes(StandardCharsets.UTF_8), 1, false);

            assertTrue(received.await(5, TimeUnit.SECONDS));
            assertEquals(identity, inbound.get().device());
            assertEquals(DeviceMessageType.PROPERTY_REPORT, inbound.get().type());
            assertEquals("tcp://127.0.0.1:" + port, inbound.get().metadata().get("brokerUri"));

            transport.send(identity.routingKey(), ByteBuffer.wrap("stop".getBytes(StandardCharsets.UTF_8)));
            assertTrue(commandReceived.await(5, TimeUnit.SECONDS));
            assertArrayEquals("stop".getBytes(StandardCharsets.UTF_8), command.get());
            assertEquals(1, transport.snapshot().receivedMessages());
            assertEquals(1, transport.snapshot().sentMessages());
            assertEquals(1, transport.snapshot().activeConnections());
        } finally {
            if (robot.isConnected()) robot.disconnect();
            robot.close();
            transport.close();
            broker.stop();
        }
        assertFalse(transport.isRunning());
    }

    @Test
    void rejectsMessagesFromUnknownDevices() throws Exception {
        MqttClientTopicMapper.MappedTopic mapped = new MqttClientTopicMapper()
            .map("devices/factory/robot-02/events/alarm");
        assertEquals(new DeviceIdentity("factory", "robot-02"), mapped.device());
        assertEquals(DeviceMessageType.EVENT_REPORT, mapped.type());
        assertEquals("alarm", mapped.detail());
        assertThrows(IllegalArgumentException.class, () -> new MqttClientTopicMapper().map("unknown/topic"));
    }

    private static TransportProvider.TransportMessageHandler handler(AtomicReference<DeviceMessage> result,
                                                                       CountDownLatch received) {
        return new TransportProvider.TransportMessageHandler() {
            @Override public void connected(String connectionId, ProtocolContext context) { }
            @Override public void received(String connectionId, ByteBuffer payload, ProtocolContext context) { }
            @Override public void received(String connectionId, DeviceMessage message, ProtocolContext context) {
                result.set(message);
                received.countDown();
            }
            @Override public void disconnected(String connectionId, ProtocolContext context, Throwable cause) { }
        };
    }

    private static DeviceRegistry registry(RegisteredDevice registered) {
        return new DeviceRegistry() {
            @Override public Optional<RegisteredDevice> find(DeviceIdentity identity) {
                return registered.identity().equals(identity) ? Optional.of(registered) : Optional.empty();
            }
            @Override public RegisteredDevice save(RegisteredDevice device) { return device; }
            @Override public boolean authenticate(DeviceIdentity identity, DeviceCredential credential) { return false; }
        };
    }

    private static int availablePort() throws Exception {
        try (ServerSocket socket = new ServerSocket(0)) { return socket.getLocalPort(); }
    }
}
