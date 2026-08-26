package org.ssssssss.magicapi.iot.transport.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.ObservableTransportProvider;
import org.ssssssss.magicapi.iot.core.spi.TransportSnapshot;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class NettyTcpTransportProvider implements ObservableTransportProvider {
    private final TcpTransportProperties properties;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Map<String, Channel> channels = new ConcurrentHashMap<>();
    private final Map<String, ProtocolContext> contexts = new ConcurrentHashMap<>();
    private final AtomicLong acceptedConnections = new AtomicLong();
    private final AtomicLong receivedMessages = new AtomicLong();
    private final AtomicLong receivedBytes = new AtomicLong();
    private final AtomicLong sentMessages = new AtomicLong();
    private final AtomicLong sentBytes = new AtomicLong();
    private final AtomicLong errors = new AtomicLong();
    private volatile Channel serverChannel;
    private volatile int boundPort;
    private volatile TransportMessageHandler handler;

    public NettyTcpTransportProvider(TcpTransportProperties properties) {
        this.properties = properties;
    }

    @Override
    public String transportId() { return "tcp"; }

    @Override
    public synchronized void start(TransportMessageHandler handler) {
        if (isRunning()) return;
        this.handler = handler;
        ServerBootstrap bootstrap = new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) {
                    channel.pipeline().addLast(new LineBasedFrameDecoder(properties.getMaxFrameLength(), true, true));
                    channel.pipeline().addLast(new InboundHandler());
                }
            });
        serverChannel = bootstrap.bind(properties.getHost(), properties.getPort()).syncUninterruptibly().channel();
        boundPort = ((InetSocketAddress) serverChannel.localAddress()).getPort();
    }

    @Override
    public void send(String connectionId, ByteBuffer payload) {
        Channel channel = channels.get(connectionId);
        if (channel == null || !channel.isActive()) throw new IllegalArgumentException("Unknown TCP connection: " + connectionId);
        ByteBuffer source = payload.asReadOnlyBuffer();
        int length = source.remaining();
        ByteBuf buffer = Unpooled.wrappedBuffer(source);
        channel.writeAndFlush(buffer).addListener(future -> {
            if (future.isSuccess()) {
                sentMessages.incrementAndGet();
                sentBytes.addAndGet(length);
            } else {
                errors.incrementAndGet();
            }
        });
    }

    @Override
    public void disconnect(String connectionId) {
        Channel channel = channels.get(connectionId);
        if (channel != null) channel.close();
    }

    @Override
    public boolean isRunning() { return serverChannel != null && serverChannel.isActive(); }

    @Override
    public TransportSnapshot snapshot() {
        return new TransportSnapshot(transportId(), isRunning(), properties.getHost(), boundPort,
            channels.size(), acceptedConnections.get(), receivedMessages.get(), receivedBytes.get(),
            sentMessages.get(), sentBytes.get(), errors.get());
    }

    @Override
    public synchronized void close() {
        clients.close().awaitUninterruptibly();
        if (serverChannel != null) serverChannel.close().awaitUninterruptibly();
        bossGroup.shutdownGracefully().awaitUninterruptibly();
        workerGroup.shutdownGracefully().awaitUninterruptibly();
        channels.clear();
        contexts.clear();
        boundPort = 0;
    }

    private final class InboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        public void channelActive(ChannelHandlerContext context) {
            Channel channel = context.channel();
            String connectionId = channel.id().asLongText();
            String remoteAddress = remoteAddress(channel);
            ProtocolContext protocolContext = new ProtocolContext("tcp", remoteAddress,
                new DeviceIdentity("tcp-raw", connectionId), Map.of("connectionId", connectionId));
            clients.add(channel);
            channels.put(connectionId, channel);
            contexts.put(connectionId, protocolContext);
            acceptedConnections.incrementAndGet();
            handler.connected(connectionId, protocolContext);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext context, ByteBuf message) {
            String connectionId = context.channel().id().asLongText();
            byte[] bytes = new byte[message.readableBytes()];
            message.readBytes(bytes);
            receivedMessages.incrementAndGet();
            receivedBytes.addAndGet(bytes.length);
            handler.received(connectionId, ByteBuffer.wrap(bytes), contexts.get(connectionId));
        }

        @Override
        public void channelInactive(ChannelHandlerContext context) {
            String connectionId = context.channel().id().asLongText();
            channels.remove(connectionId);
            ProtocolContext protocolContext = contexts.remove(connectionId);
            handler.disconnected(connectionId, protocolContext, null);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
            errors.incrementAndGet();
            context.close();
        }
    }

    private static String remoteAddress(Channel channel) {
        if (channel.remoteAddress() instanceof InetSocketAddress address) {
            return address.getAddress().getHostAddress() + ":" + address.getPort();
        }
        return String.valueOf(channel.remoteAddress());
    }
}
