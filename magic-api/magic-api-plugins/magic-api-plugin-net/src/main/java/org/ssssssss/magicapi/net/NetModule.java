package org.ssssssss.magicapi.net;

import io.netty.channel.Channel;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.magicapi.netty.NettyService;
import org.ssssssss.magicapi.netty.NettyService.TransferMode;

import org.ssssssss.magicapi.netty.WebSocketAuthProvider;
import org.ssssssss.magicapi.net.auth.RuoYiWebSocketAuthProvider;
import org.ssssssss.magicapi.net.hub.WebSocketHub;
import org.ssssssss.magicapi.net.service.NetServerManager;
import org.ssssssss.script.functions.DynamicAttribute;
import org.ssssssss.script.functions.DynamicMethod;

import javax.net.ssl.SSLContext;
import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Net 模块
 * 提供基于 Netty 的 TCP/UDP 网络编程功能
 * 支持 Text 和 Binary 两种传输模式
 *
 * 使用方法：
 * // TCP 服务端 (带别名)
 * var channel = net.createServer("myServer", "tcp", 8080)
 * net.onMessage((clientId, msg) => {
 *     println("收到: " + msg)
 *     net.broadcast("Hello " + clientId)
 * })
 *
 * // TCP 客户端 (带别名)
 * var client = net.createClient("myClient", "tcp", "127.0.0.1", 8080)
 *
 * // 获取已创建的连接
 * var channel = net.get("myServer")
 *
 * // 关闭连接
 * net.close("myServer")
 */
@MagicModule("net")
public class NetModule implements DynamicMethod, DynamicAttribute<NetModule, NetModule> {

    private final NettyService nettyService = NetServerManager.getNettyService();

    // 当前会话的消息处理器（用于新创建的连接）
    private BiConsumer<String, Object> messageHandler;
    private Runnable connectedHandler;
    private BiConsumer<String, Object> disconnectedHandler;

    // 当前会话的传输模式
    private TransferMode transferMode = TransferMode.TEXT;

    /**
     * 连接信息（用于 DynamicAttribute 返回）
     */
    public static class ChannelInfo {
        final String name;
        final String type;  // "tcp-server", "tcp-client", "udp-server", "udp-client", "websocket-server", "websocket-client"
        final Channel channel;
        final TransferMode transferMode;
        final long createTime;
        volatile BiConsumer<String, Object> messageHandler;

        public ChannelInfo(String name, String type, Channel channel, TransferMode transferMode) {
            this.name = name;
            this.type = type;
            this.channel = channel;
            this.transferMode = transferMode;
            this.createTime = System.currentTimeMillis();
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public Channel getChannel() { return channel; }
        public TransferMode getTransferMode() { return transferMode; }
        public long getCreateTime() { return createTime; }
        public boolean isActive() { return channel != null && channel.isActive(); }
        public BiConsumer<String, Object> getMessageHandler() { return messageHandler; }
        public void setMessageHandler(BiConsumer<String, Object> handler) { this.messageHandler = handler; }
    }

    // 已注册的连接别名 -> 连接信息
    private static final Map<String, ChannelInfo> channelRegistry = new ConcurrentHashMap<>();

    // 已注册的 WebSocket Hub
    private static final Map<String, WebSocketHub> webSocketHubRegistry = new ConcurrentHashMap<>();

    // 已注册的连接别名 -> 消息处理器（用于动态更新）
    private static final Map<String, BiConsumer<String, Object>> channelMessageHandlers = new ConcurrentHashMap<>();

    // 已注册的连接别名 -> 连接验证器
    private static final Map<String, ConnectionValidator> connectionValidators = new ConcurrentHashMap<>();

    // 已注册的连接别名 -> 已验证的客户端（key: clientId，value: 是否允许）
    private static final Map<String, ConcurrentHashMap<String, Boolean>> authorizedClients = new ConcurrentHashMap<>();

    // 已注册的连接别名 -> 连接回调处理器 (clientId, channel) -> {}
    private static final Map<String, java.util.function.BiConsumer<String, Channel>> connectionHandlers = new ConcurrentHashMap<>();

    // 已注册的连接别名 -> 断开连接回调处理器 (clientId, channel) -> {}
    private static final Map<String, java.util.function.BiConsumer<String, Channel>> disconnectionHandlers = new ConcurrentHashMap<>();

    /**
     * 自定义函数式接口：3个参数的函数
     */
    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    // 已注册的连接别名 -> 注册回调处理器 (clientId, registerData, channel) -> alias 或 null
    private static final Map<String, TriFunction<String, Object, Channel, String>> registerHandlers = new ConcurrentHashMap<>();

    private static final WebSocketAuthProvider RUOYI_WEB_SOCKET_AUTH_PROVIDER = new RuoYiWebSocketAuthProvider();

    /**
     * 连接验证器接口
     */
    public interface ConnectionValidator {
        /**
         * 验证连接
         * @param clientId 客户端 ID（如 IP:端口）
         * @param channel Channel（可用于发送拒绝消息）
         * @return true 允许连接，false 拒绝连接
         */
        boolean validate(String clientId, Channel channel);
    }

    /**
     * 客户端注册信息
     */
    public static class ClientInfo {
        final String clientId;           // 客户端原始ID（IP:Port）
        final String alias;              // 客户端别名
        final String serverName;         // 所属服务端名称
        final Channel channel;           // 客户端 Channel
        final long connectTime;          // 连接时间
        volatile Object metadata;        // 自定义元数据（如注册时返回的信息）

        public ClientInfo(String clientId, String alias, String serverName, Channel channel) {
            this.clientId = clientId;
            this.alias = alias;
            this.serverName = serverName;
            this.channel = channel;
            this.connectTime = System.currentTimeMillis();
        }

        public String getClientId() { return clientId; }
        public String getAlias() { return alias; }
        public String getServerName() { return serverName; }
        public Channel getChannel() { return channel; }
        public long getConnectTime() { return connectTime; }
        public Object getMetadata() { return metadata; }
        public void setMetadata(Object metadata) { this.metadata = metadata; }
        public boolean isActive() { return channel != null && channel.isActive(); }
    }

    /**
     * 验证指定客户端是否已授权
     */
    public static boolean isClientAuthorized(String serverName, String clientId) {
        ConcurrentHashMap<String, Boolean> clients = authorizedClients.get(serverName);
        return clients != null && Boolean.TRUE.equals(clients.get(clientId));
    }

    /**
     * 添加已授权的客户端
     */
    public static void authorizeClient(String serverName, String clientId) {
        authorizedClients.computeIfAbsent(serverName, k -> new ConcurrentHashMap<>()).put(clientId, Boolean.TRUE);
    }

    /**
     * 移除已授权的客户端
     */
    public static void removeAuthorizedClient(String serverName, String clientId) {
        ConcurrentHashMap<String, Boolean> clients = authorizedClients.get(serverName);
        if (clients != null) {
            clients.remove(clientId);
        }
    }

    /**
     * 清空指定服务端的授权客户端
     */
    public static void clearAuthorizedClients(String serverName) {
        authorizedClients.remove(serverName);
    }

    /**
     * 获取指定服务端的已授权客户端数量
     */
    public static int getAuthorizedClientCount(String serverName) {
        ConcurrentHashMap<String, Boolean> clients = authorizedClients.get(serverName);
        return clients != null ? clients.size() : 0;
    }

    // ========== 客户端别名管理 ==========

    // 服务端名称 -> (别名 -> ClientInfo)
    private static final Map<String, ConcurrentHashMap<String, ClientInfo>> serverClientsByAlias = new ConcurrentHashMap<>();

    // 服务端名称 -> (原始clientId -> ClientInfo)
    private static final Map<String, ConcurrentHashMap<String, ClientInfo>> serverClientsById = new ConcurrentHashMap<>();

    // 全局别名 -> ClientInfo（跨服务端查找）
    private static final Map<String, ClientInfo> globalClientAliases = new ConcurrentHashMap<>();

    /**
     * 注册客户端（内部方法）
     */
    public static void registerClient(String serverName, String clientId, String alias, Channel channel) {
        ConcurrentHashMap<String, ClientInfo> clientsById = serverClientsById.computeIfAbsent(serverName, k -> new ConcurrentHashMap<>());
        ConcurrentHashMap<String, ClientInfo> clientsByAlias = serverClientsByAlias.computeIfAbsent(serverName, k -> new ConcurrentHashMap<>());

        // 移除旧的别名映射（如果有）
        ClientInfo oldInfo = clientsById.get(clientId);
        if (oldInfo != null && oldInfo.alias != null) {
            clientsByAlias.remove(oldInfo.alias);
            globalClientAliases.remove(oldInfo.alias);
        }

        // 创建新的 ClientInfo
        ClientInfo info = new ClientInfo(clientId, alias, serverName, channel);
        clientsById.put(clientId, info);

        if (alias != null && !alias.isEmpty()) {
            clientsByAlias.put(alias, info);
            globalClientAliases.put(alias, info);
        }
    }

    /**
     * 注销客户端
     */
    public static void unregisterClient(String serverName, String clientId) {
        ConcurrentHashMap<String, ClientInfo> byId = serverClientsById.get(serverName);
        if (byId != null) {
            ClientInfo info = byId.remove(clientId);
            if (info != null && info.alias != null) {
                ConcurrentHashMap<String, ClientInfo> byAlias = serverClientsByAlias.get(serverName);
                if (byAlias != null) {
                    byAlias.remove(info.alias);
                }
                globalClientAliases.remove(info.alias);
            }
        }
    }

    /**
     * 根据别名获取客户端信息
     * @param alias 客户端别名
     * @return ClientInfo
     */
    public static ClientInfo getClientByAlias(String alias) {
        return globalClientAliases.get(alias);
    }

    /**
     * 根据别名获取客户端 Channel
     * @param alias 客户端别名
     * @return Channel
     */
    public static Channel getClientChannelByAlias(String alias) {
        ClientInfo info = globalClientAliases.get(alias);
        return info != null ? info.channel : null;
    }

    /**
     * 根据服务端和原始ID获取客户端信息
     */
    public static ClientInfo getClientById(String serverName, String clientId) {
        ConcurrentHashMap<String, ClientInfo> byId = serverClientsById.get(serverName);
        return byId != null ? byId.get(clientId) : null;
    }

    /**
     * 根据服务端和别名获取客户端信息
     */
    public static ClientInfo getClientByAliasInServer(String serverName, String alias) {
        ConcurrentHashMap<String, ClientInfo> byAlias = serverClientsByAlias.get(serverName);
        return byAlias != null ? byAlias.get(alias) : null;
    }

    /**
     * 获取指定服务端的所有已注册客户端
     */
    public static Collection<ClientInfo> getClients(String serverName) {
        ConcurrentHashMap<String, ClientInfo> byId = serverClientsById.get(serverName);
        return byId != null ? byId.values() : Collections.emptyList();
    }

    /**
     * 获取指定服务端的已注册客户端数量
     */
    public static int getClientCount(String serverName) {
        ConcurrentHashMap<String, ClientInfo> byId = serverClientsById.get(serverName);
        return byId != null ? byId.size() : 0;
    }

    /**
     * 获取指定服务端的已注册别名
     */
    public static Set<String> getClientAliases(String serverName) {
        ConcurrentHashMap<String, ClientInfo> byAlias = serverClientsByAlias.get(serverName);
        return byAlias != null ? new HashSet<>(byAlias.keySet()) : Collections.emptySet();
    }

    /**
     * 获取全局已注册别名
     */
    public static Set<String> getAllClientAliases() {
        return new HashSet<>(globalClientAliases.keySet());
    }

    /**
     * 获取已注册的连接信息（供 NetModuleGetter 使用）
     */
    public static Object getChannelInfo(String name) {
        ChannelInfo info = channelRegistry.get(name);
        if (info == null) {
            return null;
        }
        // 返回 ChannelInfo 本身，供脚本使用
        return info;
    }

    /**
     * 设置传输模式
     * @param mode "TEXT" 或 "BINARY"
     */
    public void setTransferMode(String mode) {
        this.transferMode = "BINARY".equalsIgnoreCase(mode) ? TransferMode.BINARY : TransferMode.TEXT;
    }

    /**
     * 获取当前传输模式
     */
    public String getTransferMode() {
        return transferMode.name();
    }

    /**
     * 实现 DynamicAttribute 接口，支持通过 net.xxx 获取已注册的连接
     * 例如: net.myServer 将返回 ChannelInfo 对象
     */
    @Override
    public NetModule getDynamicAttribute(String key) {
        ChannelInfo info = channelRegistry.get(key);
        if (info != null) {
            // 返回一个特殊的包装对象，支持便捷访问
            return new ChannelModuleWrapper(info);
        }
        return null;
    }

    /**
     * ChannelModuleWrapper - 用于包装 ChannelInfo，提供便捷访问
     */
    private static class ChannelModuleWrapper extends NetModule {
        private final ChannelInfo info;

        public ChannelModuleWrapper(ChannelInfo info) {
            this.info = info;
        }

        public String name() { return info.name; }
        public String type() { return info.type; }
        public Channel channel() { return info.channel; }
        public boolean active() { return info.isActive(); }
        public String mode() { return info.transferMode.name(); }
        public long createTime() { return info.createTime; }
        public void send(Object message) {
            if (info.channel != null && info.channel.isActive()) {
                if (info.type != null && info.type.startsWith("websocket")) {
                    NetServerManager.getNettyService().sendWebSocketMessage(info.channel, message);
                } else {
                    NetServerManager.getNettyService().sendTcpMessage(info.channel, message);
                }
            }
        }
        public void broadcast(Object message) {
            if (info.channel != null && info.channel.isActive()) {
                if ("websocket-server".equals(info.type)) {
                    NetServerManager.getNettyService().broadcastWebSocket(info.channel, message);
                } else {
                    NetServerManager.getNettyService().broadcast(info.channel, message);
                }
            }
        }
        public void close() {
            if (info.channel != null && info.channel.isActive()) {
                info.channel.close();
            }
        }
        /**
         * 设置该 Channel 的消息处理器
         */
        public void onMessage(BiConsumer<String, Object> handler) {
            info.setMessageHandler(handler);
            // 更新 channelMessageHandlers
            channelMessageHandlers.put(info.name, handler);
        }

        /**
         * 设置该服务端的客户端连接回调
         */
        public void onConnect(BiConsumer<String, String> handler) {
            onConnect(info.name, handler);
        }

        /**
         * 设置该服务端的客户端断开连接回调
         */
        public void onDisconnect(BiConsumer<String, String> handler) {
            onDisconnect(info.name, handler);
        }

        /**
         * 设置该服务端的客户端注册回调
         * 返回的字符串将作为客户端的别名
         * 返回 null 表示拒绝注册
         */
        public void onRegister(BiFunction<String, Object, String> handler) {
            onRegister(info.name, handler);
        }

        /**
         * 获取该服务端的已注册客户端数量
         */
        public int getClientCount() {
            return getClientCount(info.name);
        }

        /**
         * 获取该服务端的已注册客户端别名列表
         */
        public Set<String> getClientAliases() {
            return getClientAliases(info.name);
        }

        /**
         * 获取该服务端的已注册客户端信息
         */
        public Collection<ClientInfo> getClients() {
            return getClients(info.name);
        }

        /**
         * 发送消息给指定别名的客户端
         */
        public boolean sendTo(String clientAlias, Object message) {
            return sendTo(info.name, clientAlias, message);
        }

        /**
         * 广播消息给该服务端的指定别名列表的客户端
         */
        public void broadcastTo(Collection<String> clientAliases, Object message) {
            broadcastTo(info.name, clientAliases, message);
        }

        /**
         * 排除指定客户端后广播
         */
        public void broadcastExcept(String excludeAlias, Object message) {
            broadcastExcept(info.name, excludeAlias, message);
        }
    }

    // ==================== 统一创建接口 ====================

    /**
     * 创建 TCP 服务端并注册别名
     * @param name 别名
     * @param port 端口
     * @return Channel
     */
    public Channel createServer(String name, int port) {
        return createServer(name, "tcp", port);
    }

    /**
     * 创建服务端并注册别名
     * @param name 别名
     * @param type "tcp" 或 "udp"
     * @param port 端口
     * @return Channel
     */
    public Channel createServer(String name, String type, int port) {
        return createServer(name, type, port, transferMode);
    }

    /**
     * 创建服务端并注册别名（指定传输模式）
     */
    public Channel createServer(String name, String type, int port, String modeStr) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return createServer(name, type, port, mode);
    }

    /**
     * 创建服务端并注册别名（完整参数）
     */
    public Channel createServer(String name, String type, int port, TransferMode mode) {
        // 如果已存在，先关闭
        ChannelInfo existing = channelRegistry.get(name);
        if (existing != null) {
            closeChannel(existing);
        }

        String fullType = type + "-server";
        Channel channel;

        if ("tcp-server".equals(fullType)) {
            channel = createTcpServer(name, port, mode);
        } else if ("udp-server".equals(fullType)) {
            channel = createUdpServer(name, port, mode);
        } else if ("websocket-server".equals(fullType) || "ws-server".equals(fullType)) {
            fullType = "websocket-server";
            channel = createWebSocketServer(name, port, "/websocket", true);
        } else {
            throw new IllegalArgumentException("不支持的服务端类型: " + type);
        }

        if (channel != null && channel.isActive()) {
            channelRegistry.put(name, new ChannelInfo(name, fullType, channel, mode));
        }
        return channel;
    }

    /**
     * 创建 TCP 客户端并注册别名
     * @param name 别名
     * @param host 主机地址
     * @param port 端口
     * @return Channel
     */
    public Channel createClient(String name, String host, int port) {
        return createClient(name, "tcp", host, port);
    }

    /**
     * 创建客户端并注册别名
     * @param name 别名
     * @param type "tcp" 或 "udp"
     * @param host 主机地址
     * @param port 端口
     * @return Channel
     */
    public Channel createClient(String name, String type, String host, int port) {
        return createClient(name, type, host, port, transferMode);
    }

    /**
     * 创建客户端并注册别名（指定传输模式）
     */
    public Channel createClient(String name, String type, String host, int port, String modeStr) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return createClient(name, type, host, port, mode);
    }

    /**
     * 创建 UDP 客户端并注册别名
     * @param name 别名
     * @param localPort 本地端口，0表示随机端口
     * @return Channel
     */
    public Channel createUdpClient(String name, int localPort) {
        return createUdpClient(name, localPort, transferMode);
    }

    /**
     * 创建 UDP 客户端并注册别名（指定传输模式）
     */
    public Channel createUdpClient(String name, int localPort, String modeStr) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return createUdpClient(name, localPort, mode);
    }

    /**
     * 创建 UDP 客户端并注册别名（完整参数）
     */
    public Channel createUdpClient(String name, int localPort, TransferMode mode) {
        // 如果已存在，先关闭
        ChannelInfo existing = channelRegistry.get(name);
        if (existing != null) {
            closeChannel(existing);
        }

        // 使用内部方法创建 UDP 客户端
        Channel channel = createUdpClientInternal(localPort, mode);

        if (channel != null && channel.isActive()) {
            channelRegistry.put(name, new ChannelInfo(name, "udp-client", channel, mode));
        }
        return channel;
    }

    /**
     * 创建 TCP 客户端并注册别名
     */
    public Channel createTcpClient(String name, String host, int port) {
        return createTcpClient(name, host, port, transferMode);
    }

    /**
     * 创建 TCP 客户端并注册别名（指定传输模式）
     */
    public Channel createTcpClient(String name, String host, int port, String modeStr) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return createTcpClient(name, host, port, mode);
    }

    /**
     * 创建 TCP 客户端并注册别名（完整参数）
     */
    public Channel createTcpClient(String name, String host, int port, TransferMode mode) {
        // 如果已存在，先关闭
        ChannelInfo existing = channelRegistry.get(name);
        if (existing != null) {
            closeChannel(existing);
        }

        Channel channel = createTcpClientInternal(host, port, mode);

        if (channel != null && channel.isActive()) {
            channelRegistry.put(name, new ChannelInfo(name, "tcp-client", channel, mode));
        }
        return channel;
    }

    /**
     * 创建通用客户端并注册别名（完整参数）
     */
    public Channel createClient(String name, String type, String host, int port, TransferMode mode) {
        // 如果已存在，先关闭
        ChannelInfo existing = channelRegistry.get(name);
        if (existing != null) {
            closeChannel(existing);
        }

        String fullType = type + "-client";
        Channel channel;

        if ("tcp-client".equals(fullType)) {
            channel = createTcpClientInternal(host, port, mode);
        } else if ("udp-client".equals(fullType)) {
            channel = createUdpClientInternal(port, mode);
        } else if ("websocket-client".equals(fullType) || "ws-client".equals(fullType)) {
            fullType = "websocket-client";
            String url = buildWebSocketUrl(host, port, "/websocket", false);
            channel = createWebSocketClientInternal(url, Collections.emptyMap());
        } else {
            throw new IllegalArgumentException("不支持的客户端类型: " + type);
        }

        if (channel != null && channel.isActive()) {
            channelRegistry.put(name, new ChannelInfo(name, fullType, channel, mode));
        }
        return channel;
    }

    // ==================== 获取/关闭连接 ====================

    /**
     * 通过别名获取 Channel
     */
    public Channel get(String name) {
        ChannelInfo info = channelRegistry.get(name);
        return info != null ? info.channel : null;
    }

    /**
     * 通过别名检查连接是否存在
     */
    public boolean exists(String name) {
        return channelRegistry.containsKey(name);
    }

    /**
     * 通过别名检查连接是否活跃
     */
    public boolean isActive(String name) {
        ChannelInfo info = channelRegistry.get(name);
        return info != null && info.channel.isActive();
    }

    /**
     * 通过别名关闭连接
     */
    public void close(String name) {
        ChannelInfo info = channelRegistry.remove(name);
        if (info != null) {
            closeChannel(info);
        }
    }

    /**
     * 关闭所有连接
     */
    public void closeAll() {
        for (ChannelInfo info : channelRegistry.values()) {
            closeChannel(info);
        }
        channelRegistry.clear();
    }

    private void closeChannel(ChannelInfo info) {
        if (info.channel != null && info.channel.isActive()) {
            try {
                nettyService.closeChannel(info.channel);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    // ==================== 获取连接信息 ====================

    /**
     * 获取连接信息
     */
    public Map<String, Object> getInfo(String name) {
        ChannelInfo info = channelRegistry.get(name);
        if (info == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("name", info.name);
        result.put("type", info.type);
        result.put("active", info.channel.isActive());
        result.put("transferMode", info.transferMode.name());
        result.put("createTime", info.createTime);
        if (info.channel.localAddress() != null) {
            result.put("localAddress", info.channel.localAddress().toString());
        }
        if (info.channel.remoteAddress() != null) {
            result.put("remoteAddress", info.channel.remoteAddress().toString());
        }
        return result;
    }

    /**
     * 获取所有已注册的连接名称
     */
    public Set<String> getNames() {
        return new java.util.HashSet<>(channelRegistry.keySet());
    }

    // ==================== TCP Server ====================

    private Channel createTcpServer(String name, int port, TransferMode mode) {
        try {
            return nettyService.startTcpServer(
                port, mode, false, null, null, false,
                (clientId, msg) -> {
                    // 优先使用该 Channel 专属的 handler
                    BiConsumer<String, Object> handler = channelMessageHandlers.get(name);
                    if (handler != null) {
                        handler.accept(clientId, msg);
                    } else if (messageHandler != null) {
                        messageHandler.accept(clientId, msg);
                    }
                },
                (clientId, channel) -> {
                    // 调用连接回调
                    java.util.function.BiConsumer<String, Channel> handler = connectionHandlers.get(name);
                    if (handler != null) {
                        handler.accept(clientId, channel);
                    }
                    // 调用全局连接回调
                    if (connectedHandler != null) {
                        connectedHandler.run();
                    }
                },
                (clientId, channel) -> {
                    // 调用断开连接回调
                    java.util.function.BiConsumer<String, Channel> handler = disconnectionHandlers.get(name);
                    if (handler != null) {
                        handler.accept(clientId, channel);
                    }
                    // 注销客户端
                    unregisterClient(name, clientId);
                    // 调用全局断开回调
                    if (disconnectedHandler != null) {
                        disconnectedHandler.accept(clientId, null);
                    }
                },
                (clientId, registerData, channel) -> {
                    // 调用注册回调
                    TriFunction<String, Object, Channel, String> handler = registerHandlers.get(name);
                    if (handler != null) {
                        return handler.apply(clientId, registerData, channel);
                    }
                    // 如果没有注册回调，自动分配别名
                    String alias = "client_" + clientId.hashCode();
                    registerClient(name, clientId, alias, channel);
                    return alias;
                }
            );
        } catch (Exception e) {
            throw new RuntimeException("创建 TCP 服务端失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建 TCP 服务端（兼容旧API）
     */
    public Channel tcpServer(int port) {
        return tcpServer(port, false, null, null);
    }

    public Channel tcpServer(int port, boolean useSSL, String keystore, String keystorePassword) {
        return tcpServer(port, transferMode, useSSL, keystore, keystorePassword);
    }

    public Channel tcpServer(int port, String modeStr) {
        return tcpServer(port, modeStr, false, null, null);
    }

    public Channel tcpServer(int port, String modeStr, boolean useSSL, String keystore, String keystorePassword) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return tcpServer(port, mode, useSSL, keystore, keystorePassword);
    }

    public Channel tcpServer(int port, TransferMode mode, boolean useSSL, String keystore, String keystorePassword) {
        try {
            return nettyService.startTcpServer(
                port, mode, useSSL, keystore, keystorePassword, false,
                (clientId, msg) -> {
                    if (messageHandler != null) {
                        messageHandler.accept(clientId, msg);
                    }
                },
                (clientId, channel) -> {
                    if (connectedHandler != null) {
                        connectedHandler.run();
                    }
                },
                (clientId, channel) -> {
                    if (disconnectedHandler != null) {
                        disconnectedHandler.accept(clientId, null);
                    }
                },
                (clientId, data, channel) -> "client_" + clientId.hashCode()
            );
        } catch (Exception e) {
            throw new RuntimeException("创建 TCP 服务端失败: " + e.getMessage(), e);
        }
    }

    // ==================== TCP Client ====================

    // ==================== 内部创建方法 ====================

    private Channel createTcpClientInternal(String host, int port, TransferMode mode) {
        try {
            return nettyService.connectTcp(
                host, port, mode, false, null, null,
                (clientId, msg) -> {
                    if (messageHandler != null) {
                        messageHandler.accept(clientId, msg);
                    }
                },
                () -> {
                    if (connectedHandler != null) {
                        connectedHandler.run();
                    }
                }
            );
        } catch (Exception e) {
            throw new RuntimeException("连接 TCP 服务器失败: " + e.getMessage(), e);
        }
    }

    public Channel tcpClient(String host, int port) {
        return tcpClient(host, port, false, null, null);
    }

    public Channel tcpClient(String host, int port, boolean useSSL, String truststore, String truststorePassword) {
        return tcpClient(host, port, transferMode, useSSL, truststore, truststorePassword);
    }

    public Channel tcpClient(String host, int port, String modeStr) {
        return tcpClient(host, port, modeStr, false, null, null);
    }

    public Channel tcpClient(String host, int port, String modeStr, boolean useSSL, String truststore, String truststorePassword) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return tcpClient(host, port, mode, useSSL, truststore, truststorePassword);
    }

    public Channel tcpClient(String host, int port, TransferMode mode, boolean useSSL, String truststore, String truststorePassword) {
        try {
            return nettyService.connectTcp(
                host, port, mode, useSSL, truststore, truststorePassword,
                (clientId, msg) -> {
                    if (messageHandler != null) {
                        messageHandler.accept(clientId, msg);
                    }
                },
                () -> {
                    if (connectedHandler != null) {
                        connectedHandler.run();
                    }
                }
            );
        } catch (Exception e) {
            throw new RuntimeException("连接 TCP 服务器失败: " + e.getMessage(), e);
        }
    }

    // ==================== UDP Server ====================

    private Channel createUdpServer(String name, int port, TransferMode mode) {
        try {
            return nettyService.startUdpServer(port, mode, (clientId, msg) -> {
                // 优先使用该 Channel 专属的 handler
                BiConsumer<String, Object> handler = channelMessageHandlers.get(name);
                if (handler != null) {
                    handler.accept(clientId, msg);
                } else if (messageHandler != null) {
                    messageHandler.accept(clientId, msg);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("创建 UDP 服务端失败: " + e.getMessage(), e);
        }
    }

    public Channel udpServer(int port) {
        return udpServer(port, transferMode);
    }

    public Channel udpServer(int port, String modeStr) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return udpServer(port, mode);
    }

    public Channel udpServer(int port, TransferMode mode) {
        try {
            return nettyService.startUdpServer(port, mode, (clientId, msg) -> {
                if (messageHandler != null) {
                    messageHandler.accept(clientId, msg);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("创建 UDP 服务端失败: " + e.getMessage(), e);
        }
    }

    // ==================== UDP Client ====================

    private Channel createUdpClientInternal(int localPort, TransferMode mode) {
        try {
            return nettyService.startUdpClient(localPort, mode, (clientId, msg) -> {
                if (messageHandler != null) {
                    messageHandler.accept(clientId, msg);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("创建 UDP 客户端失败: " + e.getMessage(), e);
        }
    }

    public Channel udpClient() {
        return udpClient(0);
    }

    public Channel udpClient(int localPort) {
        return udpClient(localPort, transferMode);
    }

    public Channel udpClient(int localPort, String modeStr) {
        TransferMode mode = "BINARY".equalsIgnoreCase(modeStr) ? TransferMode.BINARY : TransferMode.TEXT;
        return udpClient(localPort, mode);
    }

    public Channel udpClient(int localPort, TransferMode mode) {
        try {
            return nettyService.startUdpClient(localPort, mode, (clientId, msg) -> {
                if (messageHandler != null) {
                    messageHandler.accept(clientId, msg);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("创建 UDP 客户端失败: " + e.getMessage(), e);
        }
    }

    // ==================== WebSocket Server ====================

    public Channel createWebSocketServer(String name, int port) {
        return createWebSocketServer(name, port, "/websocket", true);
    }

    public Channel createWebSocketServer(String name, int port, String path) {
        return createWebSocketServer(name, port, path, true);
    }

    public Channel createWebSocketServer(String name, int port, String path, boolean ruoyiAuth) {
        ChannelInfo existing = channelRegistry.get(name);
        if (existing != null) {
            closeChannel(existing);
            channelRegistry.remove(name);
        }
        Channel channel = createWebSocketServerInternal(name, port, path, ruoyiAuth);
        if (channel != null && channel.isActive()) {
            channelRegistry.put(name, new ChannelInfo(name, "websocket-server", channel, TransferMode.TEXT));
        }
        return channel;
    }

    public Channel websocketServer(int port) {
        return createWebSocketServer("websocket-server-" + port, port, "/websocket", true);
    }

    public Channel websocketServer(int port, String path) {
        return createWebSocketServer("websocket-server-" + port, port, path, true);
    }

    public Channel websocketServer(int port, String path, boolean ruoyiAuth) {
        return createWebSocketServer("websocket-server-" + port, port, path, ruoyiAuth);
    }

    // ==================== WebSocket Hub ====================

    public WebSocketHub websocketHub(int port) {
        return websocketHub("websocket-hub-" + port, port, true);
    }

    public WebSocketHub websocketHub(int port, boolean ruoyiAuth) {
        return websocketHub("websocket-hub-" + port, port, ruoyiAuth);
    }

    public WebSocketHub createWebSocketHub(String name, int port) {
        return websocketHub(name, port, true);
    }

    public WebSocketHub createWebSocketHub(String name, int port, boolean ruoyiAuth) {
        return websocketHub(name, port, ruoyiAuth);
    }

    public WebSocketHub websocketHub(String name, int port, boolean ruoyiAuth) {
        closeWebSocketHub(name);
        WebSocketHub hub = new WebSocketHub(name, port, ruoyiAuth, nettyService).start();
        webSocketHubRegistry.put(name, hub);
        return hub;
    }

    public WebSocketHub getWebSocketHub(String name) {
        return webSocketHubRegistry.get(name);
    }

    public void closeWebSocketHub(String name) {
        WebSocketHub hub = webSocketHubRegistry.remove(name);
        if (hub != null) {
            hub.close();
        }
    }

    private Channel createWebSocketServerInternal(String name, int port, String path, boolean ruoyiAuth) {
        try {
            return nettyService.startWebSocketServer(
                port,
                path,
                false,
                null,
                null,
                ruoyiAuth,
                ruoyiAuth ? RUOYI_WEB_SOCKET_AUTH_PROVIDER : null,
                (clientId, msg) -> {
                    BiConsumer<String, Object> handler = channelMessageHandlers.get(name);
                    if (handler != null) {
                        handler.accept(clientId, msg);
                    } else if (messageHandler != null) {
                        messageHandler.accept(clientId, msg);
                    }
                },
                (clientId, channel) -> {
                    java.util.function.BiConsumer<String, Channel> handler = connectionHandlers.get(name);
                    if (handler != null) {
                        handler.accept(clientId, channel);
                    }
                    if (connectedHandler != null) {
                        connectedHandler.run();
                    }
                },
                (clientId, channel) -> {
                    java.util.function.BiConsumer<String, Channel> handler = disconnectionHandlers.get(name);
                    if (handler != null) {
                        handler.accept(clientId, channel);
                    }
                    unregisterClient(name, clientId);
                    if (disconnectedHandler != null) {
                        disconnectedHandler.accept(clientId, null);
                    }
                },
                (clientId, authInfo, channel) -> {
                    String alias = null;
                    TriFunction<String, Object, Channel, String> handler = registerHandlers.get(name);
                    if (handler != null) {
                        alias = handler.apply(clientId, authInfo, channel);
                    }
                    if (alias == null || alias.isEmpty()) {
                        alias = authInfo != null && authInfo.getUserId() != null ? authInfo.getUserId() : "client_" + clientId.hashCode();
                    }
                    registerClient(name, clientId, alias, channel);
                    ClientInfo clientInfo = getClientById(name, clientId);
                    if (clientInfo != null) {
                        clientInfo.setMetadata(authInfo);
                    }
                    return alias;
                }
            );
        } catch (Exception e) {
            throw new RuntimeException("创建 WebSocket 服务端失败: " + e.getMessage(), e);
        }
    }

    // ==================== WebSocket Client ====================

    public Channel createWebSocketClient(String name, String url) {
        return createWebSocketClient(name, url, Collections.emptyMap());
    }

    public Channel createWebSocketClient(String name, String url, Map<String, String> headers) {
        ChannelInfo existing = channelRegistry.get(name);
        if (existing != null) {
            closeChannel(existing);
        }
        Channel channel = createWebSocketClientInternal(url, headers);
        if (channel != null && channel.isActive()) {
            channelRegistry.put(name, new ChannelInfo(name, "websocket-client", channel, TransferMode.TEXT));
        }
        return channel;
    }

    public Channel websocketClient(String url) {
        return createWebSocketClient("websocket-client-" + Math.abs(url.hashCode()), url);
    }

    public Channel websocketClient(String url, Map<String, String> headers) {
        return createWebSocketClient("websocket-client-" + Math.abs(url.hashCode()), url, headers);
    }

    private Channel createWebSocketClientInternal(String url, Map<String, String> headers) {
        try {
            return nettyService.connectWebSocket(
                url,
                headers,
                (clientId, msg) -> {
                    if (messageHandler != null) {
                        messageHandler.accept(clientId, msg);
                    }
                },
                () -> {
                    if (connectedHandler != null) {
                        connectedHandler.run();
                    }
                },
                () -> {
                    if (disconnectedHandler != null) {
                        disconnectedHandler.accept("server", null);
                    }
                }
            );
        } catch (Exception e) {
            throw new RuntimeException("连接 WebSocket 服务端失败: " + e.getMessage(), e);
        }
    }

    // ==================== 消息发送 ====================

    /**
     * 发送消息到 TCP 通道
     */
    public void send(Channel channel, Object message) {
        if (isWebSocketChannel(channel)) {
            nettyService.sendWebSocketMessage(channel, message);
        } else {
            nettyService.sendTcpMessage(channel, message);
        }
    }

    /**
     * 发送消息到别名对应的连接
     */
    public void send(String name, Object message) {
        Channel channel = get(name);
        if (channel == null) {
            throw new IllegalArgumentException("未找到名为 " + name + " 的连接");
        }
        ChannelInfo info = channelRegistry.get(name);
        if (info != null && info.type != null && info.type.startsWith("websocket")) {
            nettyService.sendWebSocketMessage(channel, message);
        } else {
            send(channel, message);
        }
    }

    /**
     * 发送消息到 UDP 通道
     */
    public void sendUdp(Channel channel, String host, int port, Object message) {
        nettyService.sendUdpMessage(channel, host, port, message);
    }

    /**
     * 点对点发送消息给指定别名的客户端
     * @param clientAlias 客户端别名
     * @param message 消息内容
     * @return 是否发送成功
     */
    public boolean sendTo(String clientAlias, Object message) {
        ClientInfo info = getClientByAlias(clientAlias);
        Channel channel = info != null ? info.channel : null;
        if (channel != null && channel.isActive()) {
            if (isWebSocketServer(info.serverName)) {
                nettyService.sendWebSocketMessage(channel, message);
            } else {
                nettyService.sendTcpMessage(channel, message);
            }
            return true;
        }
        return false;
    }

    /**
     * 点对点发送消息给指定服务端内的指定客户端
     * @param serverName 服务端名称
     * @param clientIdOrAlias 客户端ID或别名
     * @param message 消息内容
     * @return 是否发送成功
     */
    public boolean sendTo(String serverName, String clientIdOrAlias, Object message) {
        Channel channel = null;

        // 先尝试通过别名查找
        ClientInfo info = getClientByAliasInServer(serverName, clientIdOrAlias);
        if (info != null) {
            channel = info.channel;
        } else {
            // 再尝试通过原始ID查找
            info = getClientById(serverName, clientIdOrAlias);
            if (info != null) {
                channel = info.channel;
            }
        }

        if (channel != null && channel.isActive()) {
            if (isWebSocketServer(serverName)) {
                nettyService.sendWebSocketMessage(channel, message);
            } else {
                nettyService.sendTcpMessage(channel, message);
            }
            return true;
        }
        return false;
    }

    /**
     * 向指定客户端发送消息，同时指定回复回调
     * @param clientAlias 客户端别名
     * @param message 消息内容
     * @param callback 收到回复时的回调
     */
    public void sendToWithCallback(String clientAlias, Object message, BiConsumer<String, Object> callback) {
        // 设置一次性回调
        String tempKey = "callback_" + System.currentTimeMillis();
        channelMessageHandlers.put(tempKey, (clientId, msg) -> {
            callback.accept(clientId, msg);
            channelMessageHandlers.remove(tempKey);
        });
        sendTo(clientAlias, message);
    }

    /**
     * 广播消息到所有连接的客户端
     */
    public void broadcast(Object message) {
        for (Channel channel : nettyService.getServerChannels().values()) {
            if (channel.isActive()) {
                nettyService.broadcast(channel, message);
                nettyService.broadcastWebSocket(channel, message);
            }
        }
    }

    /**
     * 广播消息到指定别名的服务端所有客户端
     */
    public void broadcast(String name, Object message) {
        ChannelInfo info = channelRegistry.get(name);
        if (info != null && info.channel.isActive()) {
            if ("websocket-server".equals(info.type)) {
                nettyService.broadcastWebSocket(info.channel, message);
            } else {
                nettyService.broadcast(info.channel, message);
            }
        }
    }

    /**
     * 广播消息到指定服务端的指定别名列表的客户端
     * @param serverName 服务端名称
     * @param clientAliases 客户端别名列表
     * @param message 消息内容
     */
    public void broadcastTo(String serverName, java.util.Collection<String> clientAliases, Object message) {
        for (String alias : clientAliases) {
            sendTo(serverName, alias, message);
        }
    }

    /**
     * 排除指定客户端后广播
     * @param serverName 服务端名称
     * @param excludeAlias 要排除的客户端别名
     * @param message 消息内容
     */
    public void broadcastExcept(String serverName, String excludeAlias, Object message) {
        for (ClientInfo client : getClients(serverName)) {
            if (client.alias != null && !client.alias.equals(excludeAlias)) {
                sendTo(serverName, client.alias, message);
            }
        }
    }

    // ==================== 回调设置 ====================

    /**
     * 全局消息回调
     */
    public void onMessage(BiConsumer<String, Object> handler) {
        this.messageHandler = handler;
    }

    /**
     * 连接回调
     */
    public void onConnected(Runnable handler) {
        this.connectedHandler = handler;
    }

    /**
     * 断开连接回调
     */
    public void onDisconnected(BiConsumer<String, Object> handler) {
        this.disconnectedHandler = handler;
    }

    // ==================== 服务端回调设置（ChannelModuleWrapper） ====================

    /**
     * 服务端连接回调
     * @param serverName 服务端名称
     * @param handler (clientId, clientAlias) -> {}
     */
    public void onConnect(String serverName, java.util.function.BiConsumer<String, String> handler) {
        connectionHandlers.put(serverName, (clientId, channel) -> {
            String alias = null;
            ClientInfo info = getClientById(serverName, clientId);
            if (info != null) {
                alias = info.alias;
            }
            handler.accept(clientId, alias);
        });
    }

    /**
     * 服务端断开连接回调
     * @param serverName 服务端名称
     * @param handler (clientId, clientAlias) -> {}
     */
    public void onDisconnect(String serverName, java.util.function.BiConsumer<String, String> handler) {
        disconnectionHandlers.put(serverName, (clientId, channel) -> {
            String alias = null;
            ClientInfo info = getClientById(serverName, clientId);
            if (info != null) {
                alias = info.alias;
            }
            handler.accept(clientId, alias);
            // 注销客户端
            unregisterClient(serverName, clientId);
        });
    }

    /**
     * 服务端客户端注册回调（用于验证和分配别名）
     * 返回的字符串将作为客户端的别名
     * 返回 null 表示拒绝注册
     * @param serverName 服务端名称
     * @param handler (clientId, registerData) -> alias 或 null
     */
    public void onRegister(String serverName, java.util.function.BiFunction<String, Object, String> handler) {
        registerHandlers.put(serverName, (clientId, registerData, channel) -> {
            String alias = handler.apply(clientId, registerData);
            if (alias != null && !alias.isEmpty()) {
                // 注册客户端
                registerClient(serverName, clientId, alias, channel);
            }
            return alias;
        });
    }

    /**
     * 服务端消息回调（带别名）
     * @param serverName 服务端名称
     * @param handler (clientId, clientAlias, message) -> {}
     */
    public void onMessage(String serverName, java.util.function.BiConsumer<String, Object> handler) {
        channelMessageHandlers.put(serverName, (clientId, msg) -> {
            String alias = null;
            ClientInfo info = getClientById(serverName, clientId);
            if (info != null) {
                alias = info.alias;
            }
            handler.accept(clientId, msg);
        });
    }

    private static boolean isWebSocketServer(String serverName) {
        ChannelInfo info = channelRegistry.get(serverName);
        return info != null && "websocket-server".equals(info.type);
    }

    private static boolean isWebSocketChannel(Channel channel) {
        return channel != null && (
            channel.pipeline().get(io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.class) != null
                || channel.pipeline().get(io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.class) != null
        );
    }

    private static String buildWebSocketUrl(String host, int port, String path, boolean ssl) {
        String normalizedPath = path == null || path.trim().isEmpty() ? "/websocket" : path;
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }
        return (ssl ? "wss" : "ws") + "://" + host + ":" + port + normalizedPath;
    }

    // ==================== 连接管理 ====================

    public void close(Channel channel) {
        if (channel != null) {
            channel.close();
        }
    }

    public Map<String, Object> getServerStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("tcpServers", nettyService.getServerChannels().size());
        status.put("tcpClients", nettyService.getClientChannels().size());
        status.put("registeredChannels", channelRegistry.size());
        status.put("transferMode", transferMode.name());
        return status;
    }

    public SSLContext createServerSslContext(String keystorePath, String keystorePassword) {
        try {
            FileInputStream fin = new FileInputStream(keystorePath);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(fin, keystorePassword.toCharArray());
            fin.close();

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keystorePassword.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("创建 SSL 上下文失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Object execute(String methodName, java.util.List<Object> parameters) {
        throw new UnsupportedOperationException("Net 模块不支持动态方法调用，请直接调用模块方法");
    }
}
