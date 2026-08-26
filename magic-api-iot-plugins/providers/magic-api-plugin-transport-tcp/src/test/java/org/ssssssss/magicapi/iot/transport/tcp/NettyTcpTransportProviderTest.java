package org.ssssssss.magicapi.iot.transport.tcp;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.TransportProvider;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class NettyTcpTransportProviderTest {
    @Test
    void acceptsLineFramedMessagesAndTracksSnapshot() throws Exception {
        TcpTransportProperties properties = new TcpTransportProperties();
        properties.setHost("127.0.0.1");
        properties.setPort(0);
        NettyTcpTransportProvider transport = new NettyTcpTransportProvider(properties);
        CountDownLatch received = new CountDownLatch(1);
        AtomicReference<String> payload = new AtomicReference<>();
        try {
            transport.start(new TransportProvider.TransportMessageHandler() {
                public void connected(String connectionId, ProtocolContext context) { }
                public void received(String connectionId, ByteBuffer input, ProtocolContext context) {
                    payload.set(StandardCharsets.UTF_8.decode(input).toString());
                    received.countDown();
                }
                public void disconnected(String connectionId, ProtocolContext context, Throwable cause) { }
            });
            try (Socket socket = new Socket("127.0.0.1", transport.snapshot().port())) {
                OutputStream output = socket.getOutputStream();
                output.write("hello\n".getBytes(StandardCharsets.UTF_8));
                output.flush();
                assertTrue(received.await(3, TimeUnit.SECONDS));
            }
            assertEquals("hello", payload.get());
            assertEquals(1, transport.snapshot().receivedMessages());
            assertEquals(5, transport.snapshot().receivedBytes());
            assertEquals(1, transport.snapshot().acceptedConnections());
        } finally {
            transport.close();
        }
        assertFalse(transport.isRunning());
    }
}
