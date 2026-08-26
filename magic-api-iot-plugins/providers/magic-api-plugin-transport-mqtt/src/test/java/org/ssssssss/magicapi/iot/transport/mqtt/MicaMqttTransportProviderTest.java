package org.ssssssss.magicapi.iot.transport.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MicaMqttTransportProviderTest {
    @Test
    void acceptsMqttPublishAndMapsItToDeviceMessage() throws Exception {
        MqttTransportProperties properties = new MqttTransportProperties();
        properties.setPort(availablePort());
        DeviceRegistry registry = new DeviceRegistry() {
            public Optional<RegisteredDevice> find(DeviceIdentity identity) { return Optional.empty(); }
            public RegisteredDevice save(RegisteredDevice device) { return device; }
            public boolean authenticate(DeviceIdentity identity, DeviceCredential credential) { return false; }
        };
        MicaMqttTransportProvider transport = new MicaMqttTransportProvider(properties, registry);
        CountDownLatch received = new CountDownLatch(1);
        AtomicReference<DeviceMessage> result = new AtomicReference<>();
        try {
            transport.start(new TransportProvider.TransportMessageHandler() {
                public void connected(String connectionId, ProtocolContext context) { }
                public void received(String connectionId, ByteBuffer payload, ProtocolContext context) { }
                public void received(String connectionId, DeviceMessage message, ProtocolContext context) {
                    result.set(message);
                    received.countDown();
                }
                public void disconnected(String connectionId, ProtocolContext context, Throwable cause) { }
            });
            MqttClient client = new MqttClient("tcp://127.0.0.1:" + properties.getPort(),
                "factory/sensor-01", new MemoryPersistence());
            try {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                client.connect(options);
                client.publish("devices/factory/sensor-01/properties", "{\"temperature\":23.5}".getBytes(), 1, false);
                assertTrue(received.await(5, TimeUnit.SECONDS));
            } finally {
                if (client.isConnected()) client.disconnect();
                client.close();
            }
            assertEquals(new DeviceIdentity("factory", "sensor-01"), result.get().device());
            assertEquals(DeviceMessageType.PROPERTY_REPORT, result.get().type());
            assertEquals("mqtt", result.get().protocol());
            assertEquals("1", result.get().metadata().get("qos"));
            assertEquals(1, transport.snapshot().receivedMessages());
        } finally {
            transport.close();
        }
        assertFalse(transport.isRunning());
    }

    private static int availablePort() throws Exception {
        try (ServerSocket socket = new ServerSocket(0)) { return socket.getLocalPort(); }
    }
}
