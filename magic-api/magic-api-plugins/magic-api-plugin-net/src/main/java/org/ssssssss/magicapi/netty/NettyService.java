package org.ssssssss.magicapi.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssssssss.magicapi.net.hub.WebSocketContext;
import org.ssssssss.magicapi.net.hub.WebSocketRoute;
import org.ssssssss.magicapi.utils.JsonUtils;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 基于 Netty 的网络服务
 * 提供高性能 TCP/UDP 服务端和客户端
 * 支持 Text 和 Binary 两种收发模式
 */
public class NettyService {

    private static final Logger logger = LoggerFactory.getLogger(NettyService.class);

    /**
     * 数据传输模式
     */
    public enum TransferMode {
        TEXT,   // 文本模式：String 编解码
        BINARY  // 二进制模式：自定义协议（长度前缀）
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private EventLoopGroup udpGroup;
    private final Map<String, Channel> serverChannels = new ConcurrentHashMap<>();
    private final Map<String, Channel> clientChannels = new ConcurrentHashMap<>();
    private final Map<String, List<String>> clientConnections = new ConcurrentHashMap<>();
    // 存储每个 TCP 服务端对应的客户端 Channel（key: "tcp-server:端口", value: 客户端 Channel 列表）
    private final Map<String, List<Channel>> serverClientChannels = new ConcurrentHashMap<>();
    // 存储每个 WebSocket 服务端对应的客户端 Channel（key: "websocket-server:端口:路径", value: 客户端 Channel 列表）
    private final Map<String, List<Channel>> webSocketServerClientChannels = new ConcurrentHashMap<>();

    public NettyService() {
    }

    // ==================== WebSocket Hub Server ====================

    /**
     * 启动支持同端口多路径路由的 WebSocket Hub。
     */
    public Channel startWebSocketHub(int port, boolean useSSL, String keystore, String keystorePassword,
                                     boolean requireAuth, WebSocketAuthProvider authProvider,
                                     Function<String, WebSocketRoute> routeResolver,
                                     Function<WebSocketContext, String> registerHandler,
                                     BiConsumer<WebSocketContext, Object> messageHandler,
                                     Consumer<WebSocketContext> connectedHandler,
                                     Consumer<WebSocketContext> disconnectedHandler) {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        final String channelId = "websocket-hub:" + port;

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    if (useSSL && keystore != null) {
                        try {
                            SslContext sslContext = createServerSslContext(keystore, keystorePassword, false);
                            pipeline.addLast("ssl", sslContext.newHandler(ch.alloc()));
                        } catch (Exception e) {
                            logger.error("创建 WebSocket Hub SSL 上下文失败", e);
                        }
                    }
                    pipeline.addLast("http-codec", new HttpServerCodec());
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    pipeline.addLast("chunked-writer", new ChunkedWriteHandler());
                    pipeline.addLast("hub-handler", new WebSocketHubHandler(channelId, useSSL, requireAuth, authProvider,
                        routeResolver, registerHandler, messageHandler, connectedHandler, disconnectedHandler));
                }
            });

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            Channel channel = future.channel();
            serverChannels.put(channelId, channel);
            logger.info("WebSocket Hub 启动成功，端口: {}", port);
            return channel;
        } catch (Exception e) {
            logger.error("启动 WebSocket Hub 失败，端口: {}", port, e);
            shutdown();
            return null;
        }
    }

    private class WebSocketHubHandler extends ChannelInboundHandlerAdapter {
        private final String serverChannelId;
        private final boolean useSSL;
        private final boolean requireAuth;
        private final WebSocketAuthProvider authProvider;
        private final Function<String, WebSocketRoute> routeResolver;
        private final Function<WebSocketContext, String> registerHandler;
        private final BiConsumer<WebSocketContext, Object> messageHandler;
        private final Consumer<WebSocketContext> connectedHandler;
        private final Consumer<WebSocketContext> disconnectedHandler;
        private WebSocketServerHandshaker handshaker;
        private WebSocketContext context;

        WebSocketHubHandler(String serverChannelId, boolean useSSL, boolean requireAuth, WebSocketAuthProvider authProvider,
                            Function<String, WebSocketRoute> routeResolver,
                            Function<WebSocketContext, String> registerHandler,
                            BiConsumer<WebSocketContext, Object> messageHandler,
                            Consumer<WebSocketContext> connectedHandler,
                            Consumer<WebSocketContext> disconnectedHandler) {
            this.serverChannelId = serverChannelId;
            this.useSSL = useSSL;
            this.requireAuth = requireAuth;
            this.authProvider = authProvider;
            this.routeResolver = routeResolver;
            this.registerHandler = registerHandler;
            this.messageHandler = messageHandler;
            this.connectedHandler = connectedHandler;
            this.disconnectedHandler = disconnectedHandler;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            try {
                if (msg instanceof FullHttpRequest) {
                    handleHttpRequest(ctx, (FullHttpRequest) msg);
                } else if (msg instanceof WebSocketFrame) {
                    handleWebSocketFrame(ctx, (WebSocketFrame) msg);
                } else {
                    ctx.fireChannelRead(msg);
                    return;
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }

        private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
            io.netty.handler.codec.http.QueryStringDecoder decoder = new io.netty.handler.codec.http.QueryStringDecoder(request.uri());
            String requestPath = decoder.path();
            if (routeResolver == null || routeResolver.apply(requestPath) == null) {
                sendHttpResponse(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
            WebSocketAuthInfo authInfo = WebSocketAuthInfo.ANONYMOUS;
            if (requireAuth) {
                authInfo = authProvider == null ? null : authProvider.authenticate(request);
                if (authInfo == null || !authInfo.isAuthenticated()) {
                    sendHttpResponse(ctx, HttpResponseStatus.UNAUTHORIZED);
                    return;
                }
            }

            WebSocketContext socketContext = new WebSocketContext(requestPath, getClientId(ctx), ctx.channel(), request, authInfo);
            String alias = registerHandler == null ? defaultWebSocketAlias(socketContext.getClientId(), authInfo) : registerHandler.apply(socketContext);
            if (alias == null || alias.isEmpty()) {
                sendHttpResponse(ctx, HttpResponseStatus.UNAUTHORIZED);
                return;
            }
            socketContext.setAlias(alias);
            this.context = socketContext;
            ctx.channel().attr(WebSocketAttributes.CLIENT_ID).set(socketContext.getClientId());
            ctx.channel().attr(WebSocketAttributes.CLIENT_ALIAS).set(alias);
            ctx.channel().attr(WebSocketAttributes.AUTH_INFO).set(authInfo);
            ctx.channel().attr(WebSocketAttributes.SERVER_CHANNEL_ID).set(serverChannelId);

            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                buildWebSocketLocation(request, requestPath, useSSL), getWebSocketSubprotocols(request), true, 65536);
            handshaker = factory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                request.setUri(requestPath);
                handshaker.handshake(ctx.channel(), request.retain()).addListener(future -> {
                    if (future.isSuccess() && connectedHandler != null) {
                        connectedHandler.accept(socketContext);
                    }
                });
            }
        }

        private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
            if (context == null) {
                ctx.close();
                return;
            }
            if (frame instanceof TextWebSocketFrame) {
                if (messageHandler != null) {
                    messageHandler.accept(context, ((TextWebSocketFrame) frame).text());
                }
            } else if (frame instanceof BinaryWebSocketFrame) {
                ByteBuf content = frame.content();
                byte[] data = new byte[content.readableBytes()];
                content.readBytes(data);
                if (messageHandler != null) {
                    messageHandler.accept(context, data);
                }
            } else if (frame instanceof PingWebSocketFrame) {
                ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            } else if (frame instanceof CloseWebSocketFrame) {
                if (handshaker != null) {
                    handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
                } else {
                    ctx.close();
                }
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            if (context != null && disconnectedHandler != null) {
                disconnectedHandler.accept(context);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("WebSocket Hub 连接异常: {}", getClientId(ctx), cause);
            ctx.close();
        }

        private void sendHttpResponse(ChannelHandlerContext ctx, HttpResponseStatus status) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    // ==================== WebSocket Server ====================

    /**
     * 启动 WebSocket 服务端。
     */
    public Channel startWebSocketServer(int port, String path, boolean useSSL, String keystore, String keystorePassword,
                                        boolean requireAuth, WebSocketAuthProvider authProvider,
                                        BiConsumer<String, Object> messageHandler,
                                        java.util.function.BiConsumer<String, Channel> connectedHandler,
                                        java.util.function.BiConsumer<String, Channel> disconnectedHandler,
                                        org.ssssssss.magicapi.net.NetModule.TriFunction<String, WebSocketAuthInfo, Channel, String> registerHandler) {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        final String websocketPath = normalizeWebSocketPath(path);
        final String channelId = buildWebSocketServerId(port, websocketPath);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    if (useSSL && keystore != null) {
                        try {
                            SslContext sslContext = createServerSslContext(keystore, keystorePassword, false);
                            pipeline.addLast("ssl", sslContext.newHandler(ch.alloc()));
                        } catch (Exception e) {
                            logger.error("创建 WebSocket SSL 上下文失败", e);
                        }
                    }
                    pipeline.addLast("http-codec", new HttpServerCodec());
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    pipeline.addLast("chunked-writer", new ChunkedWriteHandler());
                    pipeline.addLast("auth", new WebSocketAuthHandler(channelId, websocketPath, requireAuth, authProvider, connectedHandler, disconnectedHandler, registerHandler));
                    pipeline.addLast("protocol", new WebSocketServerProtocolHandler(websocketPath, "Bearer", true, 65536));
                    pipeline.addLast("handler", new WebSocketServerFrameHandler(channelId, messageHandler, disconnectedHandler));
                }
            });

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            Channel channel = future.channel();
            serverChannels.put(channelId, channel);
            webSocketServerClientChannels.put(channelId, new java.util.concurrent.CopyOnWriteArrayList<>());
            logger.info("WebSocket 服务端启动成功，端口: {}，路径: {}", port, websocketPath);
            return channel;
        } catch (Exception e) {
            logger.error("启动 WebSocket 服务端失败，端口: {}，路径: {}", port, websocketPath, e);
            shutdown();
            return null;
        }
    }

    private class WebSocketAuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        private final String serverChannelId;
        private final String websocketPath;
        private final boolean requireAuth;
        private final WebSocketAuthProvider authProvider;
        private final java.util.function.BiConsumer<String, Channel> connectedHandler;
        private final java.util.function.BiConsumer<String, Channel> disconnectedHandler;
        private final org.ssssssss.magicapi.net.NetModule.TriFunction<String, WebSocketAuthInfo, Channel, String> registerHandler;

        WebSocketAuthHandler(String serverChannelId, String websocketPath, boolean requireAuth, WebSocketAuthProvider authProvider,
                             java.util.function.BiConsumer<String, Channel> connectedHandler,
                             java.util.function.BiConsumer<String, Channel> disconnectedHandler,
                             org.ssssssss.magicapi.net.NetModule.TriFunction<String, WebSocketAuthInfo, Channel, String> registerHandler) {
            this.serverChannelId = serverChannelId;
            this.websocketPath = websocketPath;
            this.requireAuth = requireAuth;
            this.authProvider = authProvider;
            this.connectedHandler = connectedHandler;
            this.disconnectedHandler = disconnectedHandler;
            this.registerHandler = registerHandler;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
            String requestPath = new io.netty.handler.codec.http.QueryStringDecoder(request.uri()).path();
            if (!websocketPath.equals(requestPath)) {
                sendHttpResponse(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
            WebSocketAuthInfo authInfo = WebSocketAuthInfo.ANONYMOUS;
            if (requireAuth) {
                authInfo = authProvider == null ? null : authProvider.authenticate(request);
                if (authInfo == null || !authInfo.isAuthenticated()) {
                    sendHttpResponse(ctx, HttpResponseStatus.UNAUTHORIZED);
                    return;
                }
            }
            String clientId = getClientId(ctx);
            String alias = registerHandler == null ? defaultWebSocketAlias(clientId, authInfo) : registerHandler.apply(clientId, authInfo, ctx.channel());
            if (alias == null || alias.isEmpty()) {
                sendHttpResponse(ctx, HttpResponseStatus.UNAUTHORIZED);
                return;
            }
            ctx.channel().attr(WebSocketAttributes.CLIENT_ID).set(clientId);
            ctx.channel().attr(WebSocketAttributes.CLIENT_ALIAS).set(alias);
            ctx.channel().attr(WebSocketAttributes.AUTH_INFO).set(authInfo);
            ctx.channel().attr(WebSocketAttributes.SERVER_CHANNEL_ID).set(serverChannelId);
            List<Channel> clients = webSocketServerClientChannels.get(serverChannelId);
            if (clients != null) {
                clients.add(ctx.channel());
            }
            if (connectedHandler != null) {
                connectedHandler.accept(clientId, ctx.channel());
            }
            request.setUri(requestPath);
            ctx.fireChannelRead(request.retain());
        }

        private void sendHttpResponse(ChannelHandlerContext ctx, HttpResponseStatus status) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private class WebSocketServerFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
        private final String serverChannelId;
        private final BiConsumer<String, Object> messageHandler;
        private final java.util.function.BiConsumer<String, Channel> disconnectedHandler;

        WebSocketServerFrameHandler(String serverChannelId, BiConsumer<String, Object> messageHandler,
                                    java.util.function.BiConsumer<String, Channel> disconnectedHandler) {
            this.serverChannelId = serverChannelId;
            this.messageHandler = messageHandler;
            this.disconnectedHandler = disconnectedHandler;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
            if (frame instanceof TextWebSocketFrame) {
                if (messageHandler != null) {
                    messageHandler.accept(getClientId(ctx), ((TextWebSocketFrame) frame).text());
                }
            } else if (frame instanceof BinaryWebSocketFrame) {
                ByteBuf content = frame.content();
                byte[] data = new byte[content.readableBytes()];
                content.readBytes(data);
                if (messageHandler != null) {
                    messageHandler.accept(getClientId(ctx), data);
                }
            } else if (frame instanceof PingWebSocketFrame) {
                ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            } else if (frame instanceof CloseWebSocketFrame) {
                ctx.close();
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            List<Channel> clients = webSocketServerClientChannels.get(serverChannelId);
            if (clients != null) {
                clients.remove(ctx.channel());
            }
            if (disconnectedHandler != null) {
                disconnectedHandler.accept(getClientId(ctx), ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("WebSocket 服务端连接异常: {}", getClientId(ctx), cause);
            ctx.close();
        }
    }

    // ==================== WebSocket Client ====================

    /**
     * 连接 WebSocket 服务端。
     */
    public Channel connectWebSocket(String url, Map<String, String> headers, BiConsumer<String, Object> messageHandler,
                                    Runnable connectedHandler, Runnable disconnectedHandler) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("WebSocket URL 无效: " + url, e);
        }
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            port = "wss".equalsIgnoreCase(scheme) ? 443 : 80;
        }
        boolean useSSL = "wss".equalsIgnoreCase(scheme);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        int finalPort = port;
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    if (useSSL) {
                        try {
                            pipeline.addLast("ssl", SslContextBuilder.forClient().build().newHandler(ch.alloc(), host, finalPort));
                        } catch (Exception e) {
                            logger.error("创建 WebSocket 客户端 SSL 上下文失败", e);
                        }
                    }
                    pipeline.addLast("http-codec", new io.netty.handler.codec.http.HttpClientCodec());
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    pipeline.addLast("protocol", new WebSocketClientProtocolHandler(
                        uri,
                        WebSocketVersion.V13,
                        null,
                        true,
                        buildWebSocketHeaders(headers),
                        65536
                    ));
                    pipeline.addLast("handler", new WebSocketClientFrameHandler(messageHandler, connectedHandler, disconnectedHandler));
                }
            });

        String clientId = "websocket-client:" + url;
        try {
            Channel channel = bootstrap.connect(host, port).sync().channel();
            clientChannels.put(clientId, channel);
            logger.info("WebSocket 客户端连接成功: {}", url);
            return channel;
        } catch (Exception e) {
            logger.error("连接 WebSocket 服务端失败: {}", url, e);
            group.shutdownGracefully();
            return null;
        }
    }

    private static class WebSocketClientFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
        private final BiConsumer<String, Object> messageHandler;
        private final Runnable connectedHandler;
        private final Runnable disconnectedHandler;

        WebSocketClientFrameHandler(BiConsumer<String, Object> messageHandler, Runnable connectedHandler, Runnable disconnectedHandler) {
            this.messageHandler = messageHandler;
            this.connectedHandler = connectedHandler;
            this.disconnectedHandler = disconnectedHandler;
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE && connectedHandler != null) {
                connectedHandler.run();
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
            if (frame instanceof TextWebSocketFrame) {
                if (messageHandler != null) {
                    messageHandler.accept("server", ((TextWebSocketFrame) frame).text());
                }
            } else if (frame instanceof BinaryWebSocketFrame) {
                ByteBuf content = frame.content();
                byte[] data = new byte[content.readableBytes()];
                content.readBytes(data);
                if (messageHandler != null) {
                    messageHandler.accept("server", data);
                }
            } else if (frame instanceof PingWebSocketFrame) {
                ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            } else if (frame instanceof CloseWebSocketFrame) {
                ctx.close();
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            if (disconnectedHandler != null) {
                disconnectedHandler.run();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("WebSocket 客户端异常", cause);
            ctx.close();
        }
    }

    // ==================== TCP Server ====================

    /**
     * 启动 TCP 服务端
     * @param port 端口
     * @param transferMode 传输模式
     * @param useSSL 是否使用SSL
     * @param keystore keystore路径
     * @param keystorePassword keystore密码
     * @param clientAuth 是否要求客户端证书
     * @param messageHandler 消息处理器
     * @param connectedHandler 连接处理器 (clientId, channel) -> {}
     * @param disconnectedHandler 断开连接处理器 (clientId, channel) -> {}
     * @param registerHandler 注册处理器 (clientId, registerData, channel) -> alias 或 null
     * @return Channel
     */
    public Channel startTcpServer(int port, TransferMode transferMode, boolean useSSL, String keystore, String keystorePassword,
                                   boolean clientAuth, BiConsumer<String, Object> messageHandler,
                                   java.util.function.BiConsumer<String, Channel> connectedHandler,
                                   java.util.function.BiConsumer<String, Channel> disconnectedHandler,
                                   org.ssssssss.magicapi.net.NetModule.TriFunction<String, Object, Channel, String> registerHandler) {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        // 先计算 channelId，这样可以在构造 Handler 时传入
        final String channelId = "tcp-server:" + port;

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();

                        // SSL
                        if (useSSL && keystore != null) {
                            try {
                                SslContext sslContext = createServerSslContext(keystore, keystorePassword, clientAuth);
                                SSLEngine engine = sslContext.newEngine(ch.alloc());
                                engine.setUseClientMode(false);
                                if (clientAuth) {
                                    engine.setNeedClientAuth(true);
                                }
                                pipeline.addLast("ssl", new SslHandler(engine));
                            } catch (Exception e) {
                                logger.error("创建 SSL 上下文失败", e);
                            }
                        }

                        // 根据传输模式添加编解码器
                        if (transferMode == TransferMode.TEXT) {
                            pipeline.addLast("decoder", new StringDecoder(StandardCharsets.UTF_8));
                            pipeline.addLast("encoder", new StringEncoder(StandardCharsets.UTF_8));
                        } else {
                            // Binary 模式：使用长度前缀协议
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
                            pipeline.addLast("binaryEncoder", new BinaryEncoder());
                            pipeline.addLast("binaryDecoder", new BinaryDecoder(messageHandler != null ? (msg, clientId) -> messageHandler.accept(clientId, msg) : null));
                        }

                        // 直接传入 channelId 和所有处理器
                        pipeline.addLast("handler", new TcpServerHandler(
                            messageHandler, connectedHandler, disconnectedHandler, registerHandler, transferMode, channelId));
                    }
                });

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            Channel channel = future.channel();
            serverChannels.put(channelId, channel);
            // 初始化该服务端的客户端 Channel 列表
            serverClientChannels.put(channelId, new java.util.concurrent.CopyOnWriteArrayList<>());
            logger.info("TCP 服务端启动成功，端口: {}，传输模式: {}", port, transferMode);
            return channel;
        } catch (Exception e) {
            logger.error("启动 TCP 服务端失败，端口: {}", port, e);
            shutdown();
            return null;
        }
    }

    /**
     * TCP 服务端处理器
     */
    private class TcpServerHandler extends ChannelInboundHandlerAdapter {
        private final BiConsumer<String, Object> messageHandler;
        private final java.util.function.BiConsumer<String, Channel> connectedHandler;
        private final java.util.function.BiConsumer<String, Channel> disconnectedHandler;
        private final org.ssssssss.magicapi.net.NetModule.TriFunction<String, Object, Channel, String> registerHandler;
        private final TransferMode transferMode;
        private final String serverChannelId;
        private String clientId;
        private boolean registered = false;

        public TcpServerHandler(BiConsumer<String, Object> messageHandler,
                                java.util.function.BiConsumer<String, Channel> connectedHandler,
                                java.util.function.BiConsumer<String, Channel> disconnectedHandler,
                                org.ssssssss.magicapi.net.NetModule.TriFunction<String, Object, Channel, String> registerHandler,
                                TransferMode transferMode,
                                String serverChannelId) {
            this.messageHandler = messageHandler;
            this.connectedHandler = connectedHandler;
            this.disconnectedHandler = disconnectedHandler;
            this.registerHandler = registerHandler;
            this.transferMode = transferMode;
            this.serverChannelId = serverChannelId;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            // 注册客户端连接
            List<Channel> clients = serverClientChannels.get(serverChannelId);
            if (clients != null) {
                clients.add(ctx.channel());
                logger.debug("TCP 客户端连接注册: {}, 当前连接数: {}", getClientId(ctx), clients.size());
            }
            this.clientId = getClientId(ctx);
            if (connectedHandler != null) {
                connectedHandler.accept(clientId, ctx.channel());
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            String clientId = getClientId(ctx);

            // 处理注册消息（第一条消息）
            if (!registered && registerHandler != null) {
                String alias = registerHandler.apply(clientId, msg, ctx.channel());
                if (alias != null) {
                    registered = true;
                } else {
                    // 注册失败，拒绝连接
                    ctx.close();
                    return;
                }
            }

            if (messageHandler != null) {
                if (transferMode == TransferMode.BINARY && msg instanceof byte[]) {
                    messageHandler.accept(clientId, msg);
                } else if (msg instanceof String) {
                    messageHandler.accept(clientId, msg);
                }
            }
            // 释放 ByteBuf 内存
            if (msg instanceof ByteBuf) {
                ((ByteBuf) msg).release();
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            // 注销客户端连接
            List<Channel> clients = serverClientChannels.get(serverChannelId);
            if (clients != null) {
                clients.remove(ctx.channel());
                logger.debug("TCP 客户端连接注销: {}, 当前连接数: {}", getClientId(ctx), clients.size());
            }
            if (disconnectedHandler != null) {
                disconnectedHandler.accept(clientId, ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("TCP 连接异常: {}", getClientId(ctx), cause);
            ctx.close();
        }

        private String getClientId(ChannelHandlerContext ctx) {
            return ctx.channel().remoteAddress().toString();
        }
    }

    // ==================== TCP Client ====================

    /**
     * 连接 TCP 服务器
     * @param host 主机地址
     * @param port 端口
     * @param transferMode 传输模式
     * @param useSSL 是否使用SSL
     * @param truststore truststore路径
     * @param truststorePassword truststore密码
     * @param messageHandler 消息处理器
     * @param connectedHandler 连接成功处理器
     * @return Channel
     */
    public Channel connectTcp(String host, int port, TransferMode transferMode, boolean useSSL, String truststore, String truststorePassword,
                              BiConsumer<String, Object> messageHandler, Runnable connectedHandler) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();

                        // SSL
                        if (useSSL && truststore != null) {
                            try {
                                SslContext sslContext = createClientSslContext(truststore, truststorePassword);
                                SSLEngine engine = sslContext.newEngine(ch.alloc());
                                engine.setUseClientMode(true);
                                pipeline.addLast("ssl", new SslHandler(engine));
                            } catch (Exception e) {
                                logger.error("创建 SSL 上下文失败", e);
                            }
                        }

                        // 根据传输模式添加编解码器
                        if (transferMode == TransferMode.TEXT) {
                            pipeline.addLast("decoder", new StringDecoder(StandardCharsets.UTF_8));
                            pipeline.addLast("encoder", new StringEncoder(StandardCharsets.UTF_8));
                        } else {
                            // Binary 模式：使用长度前缀协议
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
                            pipeline.addLast("binaryEncoder", new BinaryEncoder());
                            pipeline.addLast("binaryDecoder", new BinaryDecoder(messageHandler != null ? (msg, clientId) -> messageHandler.accept(clientId, msg) : null));
                        }
                        
                        pipeline.addLast("handler", new TcpClientHandler(messageHandler, connectedHandler, transferMode));
                    }
                });

        String clientId = "tcp-client:" + host + ":" + port;
        try {
            ChannelFuture future = bootstrap.connect(host, port);
            Channel channel = future.sync().channel();
            clientChannels.put(clientId, channel);
            clientConnections.computeIfAbsent("tcp-client:" + host + ":" + port, k -> new ArrayList<>())
                    .add(channel.id().asLongText());
            logger.info("TCP 客户端连接成功: {}:{}，传输模式: {}", host, port, transferMode);
            return channel;
        } catch (Exception e) {
            logger.error("连接 TCP 服务器失败: {}:{}", host, port, e);
            group.shutdownGracefully();
            return null;
        }
    }

    /**
     * 连接 TCP 服务器（兼容旧API，默认使用Text模式）
     */
    public Channel connectTcp(String host, int port, boolean useSSL, String truststore, String truststorePassword,
                              BiConsumer<String, String> messageHandler, Runnable connectedHandler) {
        return connectTcp(host, port, TransferMode.TEXT, useSSL, truststore, truststorePassword,
            (clientId, msg) -> messageHandler.accept(clientId, (String) msg), connectedHandler);
    }

    /**
     * TCP 客户端处理器
     */
    private static class TcpClientHandler extends ChannelInboundHandlerAdapter {
        private final BiConsumer<String, Object> messageHandler;
        private final Runnable connectedHandler;
        private final TransferMode transferMode;

        public TcpClientHandler(BiConsumer<String, Object> messageHandler, Runnable connectedHandler, TransferMode transferMode) {
            this.messageHandler = messageHandler;
            this.connectedHandler = connectedHandler;
            this.transferMode = transferMode;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            if (connectedHandler != null) {
                connectedHandler.run();
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (messageHandler != null) {
                if (transferMode == TransferMode.BINARY && msg instanceof byte[]) {
                    messageHandler.accept("server", msg);
                } else if (msg instanceof String) {
                    messageHandler.accept("server", msg);
                }
            }
            // 释放 ByteBuf 内存
            if (msg instanceof ByteBuf) {
                ((ByteBuf) msg).release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("TCP 客户端连接异常", cause);
            ctx.close();
        }
    }

    // ==================== Binary 编解码器 ====================

    /**
     * Binary 解码器：将 byte[] 解码为字节数组
     */
    public static class BinaryDecoder extends ByteToMessageDecoder {
        private final BiConsumer<Object, String> messageHandler;

        public BinaryDecoder(BiConsumer<Object, String> messageHandler) {
            this.messageHandler = messageHandler;
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
            if (in.readableBytes() > 0) {
                byte[] data = new byte[in.readableBytes()];
                in.readBytes(data);
                out.add(data);
            }
        }
    }

    /**
     * Binary 编码器：将字节数组编码为带长度前缀的格式
     */
    public static class BinaryEncoder extends MessageToByteEncoder<byte[]> {
        @Override
        protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) {
            out.writeInt(msg.length);
            out.writeBytes(msg);
        }
    }

    // ==================== UDP Server ====================

    /**
     * 启动 UDP 服务端
     * @param port 端口
     * @param transferMode 传输模式
     * @param messageHandler 消息处理器
     * @param registerHandler 注册处理器 (clientId, registerData) -> alias 或 null
     * @return Channel
     */
    public Channel startUdpServer(int port, TransferMode transferMode, BiConsumer<String, Object> messageHandler,
                                  java.util.function.BiFunction<String, Object, String> registerHandler) {
        udpGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(udpGroup)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, false)
                .option(ChannelOption.SO_RCVBUF, 65536)
                .option(ChannelOption.SO_SNDBUF, 65536)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) {
                        ch.pipeline().addLast("handler", new UdpServerHandler(messageHandler, registerHandler, transferMode));
                    }
                });

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            Channel channel = future.channel();
            String channelId = "udp-server:" + port;
            serverChannels.put(channelId, channel);
            logger.info("UDP 服务端启动成功，端口: {}，传输模式: {}", port, transferMode);
            return channel;
        } catch (Exception e) {
            logger.error("启动 UDP 服务端失败，端口: {}", port, e);
            return null;
        }
    }

    /**
     * 启动 UDP 服务端（兼容旧API）
     */
    public Channel startUdpServer(int port, TransferMode transferMode, BiConsumer<String, Object> messageHandler) {
        return startUdpServer(port, transferMode, messageHandler, null);
    }

    /**
     * 启动 UDP 服务端（兼容旧API，默认使用Text模式）
     */
    public Channel startUdpServer(int port, BiConsumer<String, String> messageHandler) {
        return startUdpServer(port, TransferMode.TEXT, (clientId, msg) -> messageHandler.accept(clientId, (String) msg));
    }

    /**
     * UDP 服务端处理器
     */
    private static class UdpServerHandler extends ChannelInboundHandlerAdapter {
        private final BiConsumer<String, Object> messageHandler;
        private final java.util.function.BiFunction<String, Object, String> registerHandler;
        private final TransferMode transferMode;
        private final Set<String> registeredClients = java.util.concurrent.ConcurrentHashMap.newKeySet();

        public UdpServerHandler(BiConsumer<String, Object> messageHandler,
                                java.util.function.BiFunction<String, Object, String> registerHandler,
                                TransferMode transferMode) {
            this.messageHandler = messageHandler;
            this.registerHandler = registerHandler;
            this.transferMode = transferMode;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (msg instanceof DatagramPacket) {
                DatagramPacket packet = (DatagramPacket) msg;
                ByteBuf content = packet.content();
                String clientAddress = packet.sender().toString();

                Object messageObj = null;
                if (transferMode == TransferMode.BINARY) {
                    byte[] data = new byte[content.readableBytes()];
                    content.readBytes(data);
                    messageObj = data;
                } else {
                    String message = content.toString(CharsetUtil.UTF_8);
                    messageObj = message;
                }

                // 检查是否是已注册的客户端
                if (registeredClients.contains(clientAddress)) {
                    if (messageHandler != null) {
                        messageHandler.accept(clientAddress, messageObj);
                    }
                } else if (registerHandler != null) {
                    // 尝试注册
                    String alias = registerHandler.apply(clientAddress, messageObj);
                    if (alias != null && !alias.isEmpty()) {
                        registeredClients.add(clientAddress);
                        logger.debug("UDP 客户端注册成功: {} -> {}", clientAddress, alias);
                    }
                } else {
                    // 没有注册处理器，自动注册为客户端地址哈希
                    registeredClients.add(clientAddress);
                    if (messageHandler != null) {
                        messageHandler.accept(clientAddress, messageObj);
                    }
                }
                // 释放 ByteBuf 内存
                packet.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("UDP 服务端异常", cause);
        }
    }

    // ==================== UDP Client ====================

    /**
     * 启动 UDP 客户端
     * @param localPort 本地端口
     * @param transferMode 传输模式
     * @param messageHandler 消息处理器
     * @return Channel
     */
    public Channel startUdpClient(int localPort, TransferMode transferMode, BiConsumer<String, Object> messageHandler) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) {
                        ch.pipeline().addLast("handler", new UdpClientHandler(messageHandler, transferMode));
                    }
                });

        String clientId = "udp-client:" + localPort;
        try {
            Channel channel;
            if (localPort > 0) {
                channel = bootstrap.bind(localPort).sync().channel();
            } else {
                channel = bootstrap.bind(0).sync().channel();
            }
            clientChannels.put(clientId, channel);
            int actualPort = channel.localAddress() instanceof InetSocketAddress
                ? ((InetSocketAddress) channel.localAddress()).getPort() : 0;
            logger.info("UDP 客户端启动成功，本地端口: {}，传输模式: {}", actualPort, transferMode);
            return channel;
        } catch (Exception e) {
            logger.error("启动 UDP 客户端失败", e);
            group.shutdownGracefully();
            return null;
        }
    }

    /**
     * 启动 UDP 客户端（兼容旧API，默认使用Text模式）
     */
    public Channel startUdpClient(int localPort, BiConsumer<String, String> messageHandler) {
        return startUdpClient(localPort, TransferMode.TEXT, (clientId, msg) -> messageHandler.accept(clientId, (String) msg));
    }

    /**
     * UDP 客户端处理器
     */
    private static class UdpClientHandler extends ChannelInboundHandlerAdapter {
        private final BiConsumer<String, Object> messageHandler;
        private final TransferMode transferMode;

        public UdpClientHandler(BiConsumer<String, Object> messageHandler, TransferMode transferMode) {
            this.messageHandler = messageHandler;
            this.transferMode = transferMode;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (msg instanceof DatagramPacket) {
                DatagramPacket packet = (DatagramPacket) msg;
                ByteBuf content = packet.content();
                
                if (messageHandler != null) {
                    if (transferMode == TransferMode.BINARY) {
                        byte[] data = new byte[content.readableBytes()];
                        content.readBytes(data);
                        messageHandler.accept("server", data);
                    } else {
                        String message = content.toString(CharsetUtil.UTF_8);
                        messageHandler.accept("server", message);
                    }
                }
                // 释放 ByteBuf 内存
                packet.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("UDP 客户端异常", cause);
        }
    }

    // ==================== 消息发送 ====================

    /**
     * 发送消息到 TCP 通道
     */
    public void sendTcpMessage(Channel channel, Object message) {
        if (channel != null && channel.isActive()) {
            if (message instanceof String) {
                channel.writeAndFlush(message);
            } else if (message instanceof byte[]) {
                ByteBuf buf = Unpooled.copiedBuffer((byte[]) message);
                channel.writeAndFlush(buf);
            }
        }
    }

    /**
     * 发送消息到 TCP 通道（兼容旧API）
     */
    public void sendTcpMessage(Channel channel, String message) {
        sendTcpMessage(channel, (Object) message);
    }

    /**
     * 发送消息到 UDP 通道
     */
    public void sendUdpMessage(Channel channel, String host, int port, Object message) {
        if (channel != null && channel.isActive()) {
            ByteBuf buf;
            if (message instanceof String) {
                buf = Unpooled.copiedBuffer((String) message, StandardCharsets.UTF_8);
            } else if (message instanceof byte[]) {
                buf = Unpooled.copiedBuffer((byte[]) message);
            } else {
                buf = Unpooled.copiedBuffer(message.toString(), StandardCharsets.UTF_8);
            }
            channel.writeAndFlush(new DatagramPacket(buf, new InetSocketAddress(host, port)));
        }
    }

    /**
     * 发送消息到 UDP 通道（兼容旧API）
     */
    public void sendUdpMessage(Channel channel, String host, int port, String message) {
        sendUdpMessage(channel, host, port, (Object) message);
    }

    /**
     * 发送 WebSocket 消息。
     */
    public void sendWebSocketMessage(Channel channel, Object message) {
        if (channel != null && channel.isActive()) {
            if (message instanceof byte[]) {
                channel.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer((byte[]) message)));
            } else if (message instanceof String || message instanceof Number || message instanceof Boolean || message instanceof Character) {
                channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(message)));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJsonStringWithoutLog(message)));
            }
        }
    }

    /**
     * 广播 WebSocket 消息到指定服务端的所有客户端。
     */
    public void broadcastWebSocket(Channel serverChannel, Object message) {
        if (serverChannel == null) {
            return;
        }
        for (Map.Entry<String, List<Channel>> entry : webSocketServerClientChannels.entrySet()) {
            Channel server = serverChannels.get(entry.getKey());
            if (server != null && server.id().asLongText().equals(serverChannel.id().asLongText())) {
                for (Channel clientChannel : entry.getValue()) {
                    sendWebSocketMessage(clientChannel, message);
                }
                logger.debug("广播 WebSocket 消息到 {} 个客户端", entry.getValue().size());
                return;
            }
        }
    }

    /**
     * 广播消息到指定服务端的所有客户端
     * @param serverChannel TCP 服务端 Channel（注意：不是监听 Channel 本身，而是包含端口信息的 Channel）
     * @param message 消息
     */
    public void broadcast(Channel serverChannel, Object message) {
        if (serverChannel == null) {
            return;
        }

        // 尝试从 Channel ID 找到对应的客户端列表
        String channelId = serverChannel.id().asLongText();

        // 遍历所有服务端，找到匹配的那个
        for (Map.Entry<String, List<Channel>> entry : serverClientChannels.entrySet()) {
            Channel server = serverChannels.get(entry.getKey());
            if (server != null && server.id().asLongText().equals(channelId)) {
                // 找到了，向所有客户端发送
                List<Channel> clients = entry.getValue();
                if (clients != null && !clients.isEmpty()) {
                    for (Channel clientChannel : clients) {
                        if (clientChannel.isActive()) {
                            sendTcpMessage(clientChannel, message);
                        }
                    }
                    logger.debug("广播消息到 {} 个客户端", clients.size());
                }
                return;
            }
        }

        // 如果上面的方式没找到，尝试通过 localAddress 匹配
        if (serverChannel.localAddress() != null) {
            String localAddr = serverChannel.localAddress().toString();
            for (Map.Entry<String, List<Channel>> entry : serverClientChannels.entrySet()) {
                Channel server = serverChannels.get(entry.getKey());
                if (server != null && server.localAddress() != null
                    && server.localAddress().toString().equals(localAddr)) {
                    List<Channel> clients = entry.getValue();
                    if (clients != null && !clients.isEmpty()) {
                        for (Channel clientChannel : clients) {
                            if (clientChannel.isActive()) {
                                sendTcpMessage(clientChannel, message);
                            }
                        }
                        logger.debug("广播消息到 {} 个客户端", clients.size());
                    }
                    return;
                }
            }
        }
    }

    /**
     * 广播消息（兼容旧API）
     */
    public void broadcast(Channel serverChannel, String message) {
        broadcast(serverChannel, (Object) message);
    }

    // ==================== 连接管理 ====================

    /**
     * 关闭通道
     */
    public void closeChannel(Channel channel) {
        if (channel != null) {
            channel.close().syncUninterruptibly();
        }
    }

    /**
     * 关闭客户端连接
     */
    public void disconnectClient(String clientId) {
        Channel channel = clientChannels.remove(clientId);
        if (channel != null) {
            channel.close();
        }
    }

    /**
     * 停止服务器
     */
    public void stopServer(String serverId) {
        Channel channel = serverChannels.remove(serverId);
        if (channel != null) {
            closeChannel(channel);
            serverClientChannels.remove(serverId);
            webSocketServerClientChannels.remove(serverId);
            logger.info("服务器已停止: {}", serverId);
        }
    }

    /**
     * 停止所有服务
     */
    public void shutdown() {
        // 关闭所有服务器通道
        for (Map.Entry<String, Channel> entry : serverChannels.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception e) {
                logger.error("关闭服务器通道失败: {}", entry.getKey(), e);
            }
        }
        serverChannels.clear();

        // 关闭所有客户端通道
        for (Map.Entry<String, Channel> entry : clientChannels.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception e) {
                logger.error("关闭客户端通道失败: {}", entry.getKey(), e);
            }
        }
        clientChannels.clear();

        // 关闭 EventLoopGroups
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (udpGroup != null) {
            udpGroup.shutdownGracefully();
        }

        logger.info("NettyService 已关闭");
    }

    private static String normalizeWebSocketPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "/websocket";
        }
        return path.startsWith("/") ? path : "/" + path;
    }

    private static String buildWebSocketServerId(int port, String path) {
        return "websocket-server:" + port + ":" + normalizeWebSocketPath(path);
    }

    private static String buildWebSocketLocation(FullHttpRequest request, String path, boolean ssl) {
        String host = request.headers().get(HttpHeaderNames.HOST);
        if (host == null || host.trim().isEmpty()) {
            host = "127.0.0.1";
        }
        return (ssl ? "wss://" : "ws://") + host + normalizeWebSocketPath(path);
    }

    private static String getWebSocketSubprotocols(FullHttpRequest request) {
        String protocols = request.headers().get("Sec-WebSocket-Protocol");
        if (protocols != null && protocols.toLowerCase().contains("bearer")) {
            return "Bearer";
        }
        return null;
    }

    private static String getClientId(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress() == null ? ctx.channel().id().asLongText() : ctx.channel().remoteAddress().toString();
    }

    private static String defaultWebSocketAlias(String clientId, WebSocketAuthInfo authInfo) {
        if (authInfo != null && authInfo.getUserId() != null && !authInfo.getUserId().isEmpty()) {
            return authInfo.getUserId();
        }
        return "client_" + clientId.hashCode();
    }

    private static io.netty.handler.codec.http.HttpHeaders buildWebSocketHeaders(Map<String, String> headers) {
        io.netty.handler.codec.http.HttpHeaders httpHeaders = new io.netty.handler.codec.http.DefaultHttpHeaders();
        if (headers != null) {
            headers.forEach((key, value) -> {
                if (key != null && value != null) {
                    httpHeaders.set(key, value);
                }
            });
        }
        return httpHeaders;
    }

    // ==================== SSL 上下文创建 ====================

    /**
     * 创建服务端 SSL 上下文
     */
    private SslContext createServerSslContext(String keystore, String keystorePassword, boolean clientAuth) throws Exception {
        FileInputStream fin = new FileInputStream(keystore);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(fin, keystorePassword.toCharArray());
        fin.close();

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keystorePassword.toCharArray());

        SslContextBuilder builder = SslContextBuilder.forServer(kmf);
        if (clientAuth) {
            builder.clientAuth(ClientAuth.REQUIRE);
        }
        return builder.build();
    }

    /**
     * 创建客户端 SSL 上下文
     */
    private SslContext createClientSslContext(String truststore, String truststorePassword) throws Exception {
        FileInputStream fin = new FileInputStream(truststore);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(fin, truststorePassword.toCharArray());
        fin.close();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        return SslContextBuilder.forClient()
                .trustManager(tmf)
                .build();
    }

    // ==================== 状态查询 ====================

    public boolean isServerRunning(String serverId) {
        Channel channel = serverChannels.get(serverId);
        return channel != null && channel.isActive();
    }

    public boolean isClientConnected(String clientId) {
        Channel channel = clientChannels.get(clientId);
        return channel != null && channel.isActive();
    }

    public int getServerPort(String serverId) {
        Channel channel = serverChannels.get(serverId);
        if (channel != null && channel.localAddress() instanceof InetSocketAddress) {
            return ((InetSocketAddress) channel.localAddress()).getPort();
        }
        return -1;
    }

    public Map<String, Channel> getServerChannels() {
        return serverChannels;
    }

    public Map<String, Channel> getClientChannels() {
        return clientChannels;
    }
}
