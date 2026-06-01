package org.dromara.common.netty.websocket.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.netty.websocket.config.properties.NettyWebSocketProperties;
import org.dromara.common.netty.websocket.handler.NettyWebSocketAuthService;
import org.dromara.common.netty.websocket.handler.NettyWebSocketServerHandler;
import org.dromara.common.netty.websocket.route.NettyWebSocketRouteRegistry;
import org.dromara.common.netty.websocket.session.NettyWebSocketSessionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * Netty WebSocket 服务。
 *
 * @author ruoyi
 */
@Slf4j
public class NettyWebSocketServer implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {

    private final NettyWebSocketProperties properties;

    private final NettyWebSocketAuthService authService;

    private final NettyWebSocketSessionManager sessionManager;

    private final NettyWebSocketRouteRegistry routeRegistry;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private Channel serverChannel;

    public NettyWebSocketServer(
        NettyWebSocketProperties properties,
        NettyWebSocketAuthService authService,
        NettyWebSocketSessionManager sessionManager,
        NettyWebSocketRouteRegistry routeRegistry) {
        this.properties = properties;
        this.authService = authService;
        this.sessionManager = sessionManager;
        this.routeRegistry = routeRegistry;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        start();
    }

    public synchronized void start() {
        if (serverChannel != null && serverChannel.isActive()) {
            return;
        }
        int workerThreads = properties.getWorkerThreads() > 0
            ? properties.getWorkerThreads()
            : Runtime.getRuntime().availableProcessors() * 2;
        bossGroup = new NioEventLoopGroup(properties.getBossThreads());
        workerGroup = new NioEventLoopGroup(workerThreads);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(
                    properties.getWriteBufferLowWaterMark(),
                    properties.getWriteBufferHighWaterMark()))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                            .addLast(new IdleStateHandler(
                                properties.getReaderIdleSeconds(),
                                properties.getWriterIdleSeconds(),
                                properties.getAllIdleSeconds()))
                            .addLast(new HttpServerCodec())
                            .addLast(new ChunkedWriteHandler())
                            .addLast(new HttpObjectAggregator(properties.getMaxFramePayloadLength()))
                            .addLast(new NettyWebSocketServerHandler(properties, authService, sessionManager, routeRegistry));
                    }
                });
            serverChannel = bootstrap.bind(properties.getPort()).sync().channel();
            log.info("Netty WebSocket started on port {}", properties.getPort());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Netty WebSocket start interrupted", e);
        } catch (Exception e) {
            stop();
            throw new IllegalStateException("Netty WebSocket start failed", e);
        }
    }

    public synchronized void stop() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel = null;
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
            bossGroup = null;
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
            workerGroup = null;
        }
    }

    @Override
    public void destroy() {
        stop();
    }
}
