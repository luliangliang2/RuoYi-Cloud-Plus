package org.ssssssss.magicapi.net.model;

import org.ssssssss.magicapi.core.model.MagicEntity;

import java.util.Map;

/**
 * Net 连接信息
 */
public class NetInfo extends MagicEntity {

    /**
     * 连接键
     */
    private String key;

    /**
     * 类型：tcp-client, tcp-server, udp-client, udp-server, websocket-client, websocket-server, websocket-hub
     */
    private String type;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 是否启用 SSL
     */
    private boolean ssl;

    /**
     * 连接超时（毫秒）
     */
    private int connectionTimeout;

    /**
     * 读超时（毫秒）
     */
    private int readTimeout;

    /**
     * 密钥库路径
     */
    private String keystore;

    /**
     * 密钥库密码
     */
    private String keystorePassword;

    /**
     * 信任库路径
     */
    private String truststore;

    /**
     * 信任库密码
     */
    private String truststorePassword;

    /**
     * 其他配置
     */
    private Map<String, Object> properties;

    /**
     * 扩展配置（如传输模式：TEXT, BINARY）
     */
    private String extend;

    public NetInfo() {
        this.connectionTimeout = 5000;
        this.readTimeout = 30000;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTruststore() {
        return truststore;
    }

    public void setTruststore(String truststore) {
        this.truststore = truststore;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Override
    public MagicEntity simple() {
        NetInfo netInfo = new NetInfo();
        netInfo.setKey(this.key);
        netInfo.setType(this.type);
        super.simple(netInfo);
        return netInfo;
    }

    @Override
    public MagicEntity copy() {
        NetInfo netInfo = new NetInfo();
        super.copyTo(netInfo);
        netInfo.setKey(key);
        netInfo.setType(this.type);
        netInfo.setProperties(properties);
        return netInfo;
    }
}
