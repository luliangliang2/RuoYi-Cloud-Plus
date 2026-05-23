package org.ssssssss.magicapi.net.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.ssssssss.magicapi.core.event.FileEvent;
import org.ssssssss.magicapi.core.service.AbstractMagicDynamicRegistry;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;
import org.ssssssss.magicapi.net.model.NetInfo;

/**
 * Net 动态注册器，处理 Net 数据源的保存和删除事件
 */
public class NetMagicDynamicRegistry extends AbstractMagicDynamicRegistry<NetInfo> {

    private static final Logger logger = LoggerFactory.getLogger(NetMagicDynamicRegistry.class);

    public NetMagicDynamicRegistry(MagicResourceStorage<NetInfo> magicResourceStorage) {
        super(magicResourceStorage);
    }

    @EventListener(condition = "#event.type == 'net'")
    public void onFileEvent(FileEvent event) {
        try {
            processEvent(event);
        } catch (Exception e) {
            logger.error("处理 Net 数据源事件失败", e);
        }
    }

    @Override
    protected boolean register(MappingNode<NetInfo> mappingNode) {
        NetInfo info = mappingNode.getEntity();
        try {
            // 对于服务端类型，启动端口监听
            if ("tcp-server".equals(info.getType()) || "udp-server".equals(info.getType())
                || "websocket-server".equals(info.getType()) || "ws-server".equals(info.getType())
                || "websocket-hub".equals(info.getType()) || "ws-hub".equals(info.getType())) {
                boolean started = NetServerManager.startServer(info);
                if (started) {
                    logger.info("Net 数据源注册成功，服务器已启动: {} (类型: {}, 端口: {})", 
                        info.getKey(), info.getType(), info.getPort());
                    return true;
                } else {
                    logger.warn("Net 数据源注册失败，无法启动服务器: {}", info.getKey());
                    return false;
                }
            } else {
                // 客户端类型只需验证配置
                logger.info("Net 数据源注册成功: {} (类型: {})", info.getKey(), info.getType());
                return true;
            }
        } catch (Exception e) {
            logger.error("注册 Net 数据源失败: {}", info.getKey(), e);
            return false;
        }
    }

    @Override
    protected void unregister(MappingNode<NetInfo> mappingNode) {
        NetInfo info = mappingNode.getEntity();
        try {
            // 停止对应的服务器
            NetServerManager.stopServer(info);
            logger.info("Net 数据源已注销，服务器已停止: {}", info.getKey());
        } catch (Exception e) {
            logger.error("注销 Net 数据源时停止服务器失败: {}", info.getKey(), e);
        }
    }
}
