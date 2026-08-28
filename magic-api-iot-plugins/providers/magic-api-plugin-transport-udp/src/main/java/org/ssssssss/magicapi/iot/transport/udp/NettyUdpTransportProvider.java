package org.ssssssss.magicapi.iot.transport.udp;

import io.netty.bootstrap.Bootstrap; import io.netty.buffer.Unpooled; import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup; import io.netty.channel.socket.DatagramPacket; import io.netty.channel.socket.nio.NioDatagramChannel;
import org.ssssssss.magicapi.iot.core.model.*; import org.ssssssss.magicapi.iot.core.spi.*;
import java.net.*; import java.nio.ByteBuffer; import java.util.Map; import java.util.concurrent.ConcurrentHashMap; import java.util.concurrent.atomic.AtomicLong;

public final class NettyUdpTransportProvider implements ObservableTransportProvider {
    private final UdpTransportProperties properties; private final EventLoopGroup group = new NioEventLoopGroup();
    private final Map<String, InetSocketAddress> remotes = new ConcurrentHashMap<>(); private final Map<String, ProtocolContext> contexts = new ConcurrentHashMap<>();
    private final AtomicLong accepted = new AtomicLong(), received = new AtomicLong(), receivedBytes = new AtomicLong(), sent = new AtomicLong(), sentBytes = new AtomicLong(), errors = new AtomicLong();
    private volatile Channel channel; private volatile int boundPort; private volatile TransportMessageHandler handler;
    public NettyUdpTransportProvider(UdpTransportProperties properties){this.properties=properties;}
    public String transportId(){return "udp";}
    public synchronized void start(TransportMessageHandler handler){ if(isRunning())return; this.handler=handler; channel=new Bootstrap().group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST,false).handler(new Inbound()).bind(properties.getHost(),properties.getPort()).syncUninterruptibly().channel(); boundPort=((InetSocketAddress)channel.localAddress()).getPort(); }
    public void send(String id, ByteBuffer payload){ InetSocketAddress remote=remotes.get(id); if(remote==null)throw new IllegalArgumentException("Unknown UDP endpoint: "+id); ByteBuffer source=payload.asReadOnlyBuffer(); int n=source.remaining(); channel.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(source),remote)).addListener(f->{if(f.isSuccess()){sent.incrementAndGet();sentBytes.addAndGet(n);}else errors.incrementAndGet();}); }
    public void disconnect(String id){ ProtocolContext context=contexts.remove(id); remotes.remove(id); if(context!=null)handler.disconnected(id,context,null); }
    public boolean isRunning(){return channel!=null&&channel.isActive();}
    public TransportSnapshot snapshot(){return new TransportSnapshot(transportId(),isRunning(),properties.getHost(),boundPort,remotes.size(),accepted.get(),received.get(),receivedBytes.get(),sent.get(),sentBytes.get(),errors.get());}
    public synchronized void close(){ if(channel!=null)channel.close().awaitUninterruptibly(); contexts.forEach((id,c)->handler.disconnected(id,c,null)); contexts.clear(); remotes.clear(); group.shutdownGracefully().awaitUninterruptibly(); boundPort=0; }
    private final class Inbound extends SimpleChannelInboundHandler<DatagramPacket>{ protected void channelRead0(ChannelHandlerContext ignored,DatagramPacket packet){ int size=packet.content().readableBytes(); if(size>properties.getMaxDatagramSize()){errors.incrementAndGet();return;} String id=address(packet.sender()); ProtocolContext context=contexts.computeIfAbsent(id,key->{accepted.incrementAndGet(); ProtocolContext created=new ProtocolContext(transportId(),id,new DeviceIdentity(properties.getProtocolId(),id),Map.of("connectionId",id,"protocolId",properties.getProtocolId())); remotes.put(id,packet.sender()); handler.connected(id,created); return created;}); byte[] bytes=new byte[size]; packet.content().readBytes(bytes); received.incrementAndGet(); receivedBytes.addAndGet(size); handler.received(id,ByteBuffer.wrap(bytes),context); } public void exceptionCaught(ChannelHandlerContext c,Throwable cause){errors.incrementAndGet();}}
    private static String address(InetSocketAddress value){return value.getAddress().getHostAddress()+":"+value.getPort();}
}
