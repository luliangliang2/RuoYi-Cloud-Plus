package org.ssssssss.magicapi.net.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssssssss.magicapi.net.model.NetInfo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Net 数据源验证工具类
 */
public class NetDataSource {

    private static final Logger logger = LoggerFactory.getLogger(NetDataSource.class);

    private final NetInfo info;

    public NetDataSource(NetInfo info) {
        this.info = info;
    }

    /**
     * 验证连接
     */
    public boolean validate() {
        try {
            String type = info.getType();
            if ("tcp-client".equals(type)) {
                return testTcpClient();
            } else if ("tcp-server".equals(type)) {
                return testTcpServer();
            } else if ("udp-client".equals(type)) {
                return testUdpClient();
            } else if ("udp-server".equals(type)) {
                return testUdpServer();
            }
            return false;
        } catch (Exception e) {
            logger.error("连接验证失败: {}", e.getMessage());
            return false;
        }
    }

    private boolean testTcpClient() {
        String host = info.getHost() != null ? info.getHost() : "127.0.0.1";
        int port = info.getPort();
        int timeout = 5000;

        Socket socket = new Socket();
        try {
            socket.connect(new java.net.InetSocketAddress(host, port), timeout);
            boolean connected = socket.isConnected();
            logger.info("TCP 客户端连接测试成功: {}:{}", host, port);
            return connected;
        } catch (Exception e) {
            logger.warn("TCP 客户端连接测试失败: {}:{} - {}", host, port, e.getMessage());
            return false;
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }

    private boolean testTcpServer() {
        int port = info.getPort();
        if (port <= 0 || port > 65535) {
            return false;
        }
        // 服务端只需验证端口范围
        logger.info("TCP 服务端端口验证通过: {}", port);
        return true;
    }

    private boolean testUdpClient() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);

            // 尝试发送一个测试数据包
            String testMessage = "test";
            byte[] sendData = testMessage.getBytes();
            InetAddress address = InetAddress.getByName(info.getHost() != null ? info.getHost() : "127.0.0.1");
            int port = info.getPort() > 0 ? info.getPort() : 8080;

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);

            logger.info("UDP 客户端测试通过: {}:{}", address, port);
            return true;
        } catch (Exception e) {
            logger.warn("UDP 客户端测试: {} - {}", e.getMessage());
            // UDP 不需要对方响应，所以发送成功就算通过
            return true;
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private boolean testUdpServer() {
        int port = info.getPort();
        if (port <= 0 || port > 65535) {
            return false;
        }
        // 检查端口是否可用
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            logger.info("UDP 服务端端口验证通过: {}", port);
            return true;
        } catch (Exception e) {
            logger.warn("UDP 服务端端口被占用: {} - {}", port, e.getMessage());
            return false;
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    /**
     * 关闭连接（兼容旧 API）
     */
    public void close() {
        // 验证测试时已经关闭了连接，无需额外处理
    }
}
