package org.ssssssss.magicapi.net.service;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssssssss.magicapi.netty.NettyService;
import org.ssssssss.magicapi.netty.NettyService.TransferMode;
import org.ssssssss.magicapi.net.model.NetInfo;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 Netty 的服务器管理器
 * 负责管理所有已启动的 TCP/UDP 服务端
 */
public class NetServerManager {

    private static final Logger logger = LoggerFactory.getLogger(NetServerManager.class);

    private static final NettyService nettyService = new NettyService();
    private static final Map<String, ServerInfo> serverCache = new ConcurrentHashMap<>();

    /**
     * 服务器信息
     */
    private static class ServerInfo {
        final Channel channel;
        final NetInfo netInfo;
        final long startTime;
        final TransferMode transferMode;

        ServerInfo(Channel channel, NetInfo netInfo, TransferMode transferMode) {
            this.channel = channel;
            this.netInfo = netInfo;
            this.startTime = System.currentTimeMillis();
            this.transferMode = transferMode;
        }
    }

    /**
     * 启动服务器
     * @param info Net 连接信息
     * @return 是否启动成功
     */
    public static boolean startServer(NetInfo info) {
        String serverId = buildServerId(info);

        // 如果服务器已存在，先停止
        if (serverCache.containsKey(serverId)) {
            stopServer(info);
        }

        try {
            String type = info.getType();
            Channel channel;
            TransferMode mode = getTransferMode(info);

            if ("tcp-server".equals(type)) {
                channel = startTcpServer(info, mode);
            } else if ("udp-server".equals(type)) {
                channel = startUdpServer(info, mode);
            } else {
                // 客户端模式不需要启动服务器
                logger.info("客户端模式不需要启动服务器: {}", info.getKey());
                return true;
            }

            if (channel != null && channel.isActive()) {
                serverCache.put(serverId, new ServerInfo(channel, info, mode));
                logger.info("Net 服务器启动成功: {} (类型: {}, 端口: {}, 模式: {})",
                    info.getKey(), info.getType(), info.getPort(), mode);
                return true;
            } else {
                logger.warn("Net 服务器启动失败: {}", info.getKey());
                return false;
            }
        } catch (Exception e) {
            logger.error("启动服务器异常: {} - {}", info.getKey(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从 NetInfo 获取传输模式
     */
    private static TransferMode getTransferMode(NetInfo info) {
        String mode = info.getExtend();
        if (mode != null && "BINARY".equalsIgnoreCase(mode)) {
            return TransferMode.BINARY;
        }
        return TransferMode.TEXT;
    }

    /**
     * 启动 TCP 服务器
     */
    private static Channel startTcpServer(NetInfo info, TransferMode mode) {
        return nettyService.startTcpServer(
            info.getPort(),
            mode,
            info.isSsl(),
            info.getKeystore(),
            info.getKeystorePassword(),
            false,  // clientAuth
            (clientId, message) -> {
                logger.debug("TCP 收到消息 from {}: {} (模式: {})", clientId, message, mode);
                // 消息处理回调，可以在这里添加自定义处理逻辑
            },
            (clientId, channel) -> {
                logger.info("TCP 客户端连接: {}", (Object)(channel != null ? channel.remoteAddress() : clientId));
            },
            (clientId, channel) -> {
                logger.info("TCP 客户端断开: {}", (Object)(channel != null ? channel.remoteAddress() : clientId));
            },
            (clientId, data, channel) -> "client_" + clientId.hashCode()
        );
    }

    /**
     * 启动 UDP 服务器
     */
    private static Channel startUdpServer(NetInfo info, TransferMode mode) {
        return nettyService.startUdpServer(
            info.getPort(),
            mode,
            (clientId, message) -> {
                logger.debug("UDP 收到消息 from {}: {} (模式: {})", clientId, message, mode);
                // 消息处理回调
            }
        );
    }

    /**
     * 停止服务器
     * @param info Net 连接信息
     */
    public static void stopServer(NetInfo info) {
        String serverId = buildServerId(info);
        ServerInfo serverInfo = serverCache.remove(serverId);
        if (serverInfo != null && serverInfo.channel != null) {
            try {
                serverInfo.channel.close();
                logger.info("Net 服务器已停止: {} (端口: {})", info.getKey(), info.getPort());
            } catch (Exception e) {
                logger.error("停止服务器异常: {}", info.getKey(), e);
            }
        }
    }

    /**
     * 发送消息到所有连接的客户端
     */
    public static void broadcast(String key, Object message) {
        for (Map.Entry<String, ServerInfo> entry : serverCache.entrySet()) {
            ServerInfo info = entry.getValue();
            if (info.netInfo.getKey().equals(key) && "tcp-server".equals(info.netInfo.getType())) {
                nettyService.sendTcpMessage(info.channel, message);
            }
        }
    }

    /**
     * 发送消息到所有连接的客户端（兼容 String 版本）
     */
    public static void broadcast(String key, String message) {
        broadcast(key, (Object) message);
    }

    /**
     * 发送消息到 UDP 服务器
     */
    public static void sendUdpMessage(String key, String host, int port, Object message) {
        ServerInfo info = serverCache.get(buildServerId(key, "udp-server", port));
        if (info != null) {
            nettyService.sendUdpMessage(info.channel, host, port, message);
        }
    }

    /**
     * 发送消息到 UDP 服务器（兼容 String 版本）
     */
    public static void sendUdpMessage(String key, String host, int port, String message) {
        sendUdpMessage(key, host, port, (Object) message);
    }

    /**
     * 检查服务器是否正在运行
     */
    public static boolean isRunning(NetInfo info) {
        String serverId = buildServerId(info);
        ServerInfo serverInfo = serverCache.get(serverId);
        return serverInfo != null && serverInfo.channel.isActive();
    }

    /**
     * 获取服务器状态
     */
    public static String getServerStatus(NetInfo info) {
        String serverId = buildServerId(info);
        ServerInfo serverInfo = serverCache.get(serverId);
        if (serverInfo == null) {
            return "未启动";
        }
        if (!serverInfo.channel.isActive()) {
            return "已停止";
        }
        int port = -1;
        if (serverInfo.channel.localAddress() instanceof InetSocketAddress) {
            port = ((InetSocketAddress) serverInfo.channel.localAddress()).getPort();
        }
        return String.format("运行中，端口: %d, 模式: %s, 运行时长: %ds",
            port,
            serverInfo.transferMode,
            (System.currentTimeMillis() - serverInfo.startTime) / 1000);
    }

    /**
     * 获取 NettyService 实例（供脚本使用）
     */
    public static NettyService getNettyService() {
        return nettyService;
    }

    /**
     * 停止所有服务器
     */
    public static void stopAll() {
        for (Map.Entry<String, ServerInfo> entry : serverCache.entrySet()) {
            try {
                entry.getValue().channel.close();
            } catch (Exception e) {
                logger.error("停止服务器异常: {}", entry.getKey(), e);
            }
        }
        serverCache.clear();
        nettyService.shutdown();
        logger.info("所有 Net 服务器已停止");
    }

    /**
     * 获取活跃服务器数量
     */
    public static int getActiveServerCount() {
        return serverCache.size();
    }

    private static String buildServerId(NetInfo info) {
        return buildServerId(info.getKey(), info.getType(), info.getPort());
    }

    private static String buildServerId(String key, String type, int port) {
        return String.format("%s-%s-%d", key, type, port);
    }
}
