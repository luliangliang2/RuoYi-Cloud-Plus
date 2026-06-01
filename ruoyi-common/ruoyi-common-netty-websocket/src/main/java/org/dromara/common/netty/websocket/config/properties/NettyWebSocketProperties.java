package org.dromara.common.netty.websocket.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Netty WebSocket 配置项。
 *
 * @author ruoyi
 */
@Data
@ConfigurationProperties("netty-websocket")
public class NettyWebSocketProperties {

    /**
     * 是否启用 Netty WebSocket 服务。
     */
    private Boolean enabled = false;

    /**
     * 监听端口。
     */
    private int port = 19090;

    /**
     * 允许连接的路径前缀。
     */
    private List<String> pathPatterns = new ArrayList<>(List.of("/ws"));

    /**
     * boss 线程数。
     */
    private int bossThreads = 1;

    /**
     * worker 线程数，0 表示 CPU * 2。
     */
    private int workerThreads = 0;

    /**
     * 单帧最大长度。
     */
    private int maxFramePayloadLength = 1024 * 1024;

    /**
     * 读空闲秒数。
     */
    private int readerIdleSeconds = 60;

    /**
     * 写空闲秒数。
     */
    private int writerIdleSeconds = 0;

    /**
     * 读写总空闲秒数。
     */
    private int allIdleSeconds = 120;

    /**
     * 写缓冲高水位。
     */
    private int writeBufferHighWaterMark = 64 * 1024;

    /**
     * 写缓冲低水位。
     */
    private int writeBufferLowWaterMark = 32 * 1024;

    /**
     * Redis 发布订阅主题。
     */
    private String redisTopic = "global:netty-websocket";

    /**
     * 是否开启鉴权。
     */
    private boolean authEnabled = true;

    /**
     * token 参数/请求头名称。
     */
    private String tokenName = "Authorization";

    /**
     * 是否允许 query token。
     */
    private boolean allowQueryToken = true;

    /**
     * 是否允许 Sec-WebSocket-Protocol 携带 token。
     */
    private boolean allowProtocolToken = true;
}
