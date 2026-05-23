package org.ssssssss.magicapi.net.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Net 模块配置属性
 *
 * 配置示例 (application.properties):
 * magic-api.net.tcp.port=8080
 * magic-api.net.tcp.ssl=false
 * magic-api.net.tcp.keystore=
 * magic-api.net.tcp.keystore-password=
 * magic-api.net.tcp.truststore=
 * magic-api.net.tcp.truststore-password=
 *
 * magic-api.net.udp.port=8081
 */
@Configuration
@ConfigurationProperties(prefix = "magic-api.net")
public class MagicNetProperties {

    private final Tcp tcp = new Tcp();
    private final Udp udp = new Udp();

    public Tcp getTcp() {
        return tcp;
    }

    public Udp getUdp() {
        return udp;
    }

    public static class Tcp {
        private boolean enabled = false;
        private int port = 8080;
        private boolean ssl = false;
        private String keystore;
        private String keystorePassword;
        private String truststore;
        private String truststorePassword;
        private int connectionTimeout = 5000;
        private int readTimeout = 30000;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public boolean isSsl() { return ssl; }
        public void setSsl(boolean ssl) { this.ssl = ssl; }
        public String getKeystore() { return keystore; }
        public void setKeystore(String keystore) { this.keystore = keystore; }
        public String getKeystorePassword() { return keystorePassword; }
        public void setKeystorePassword(String keystorePassword) { this.keystorePassword = keystorePassword; }
        public String getTruststore() { return truststore; }
        public void setTruststore(String truststore) { this.truststore = truststore; }
        public String getTruststorePassword() { return truststorePassword; }
        public void setTruststorePassword(String truststorePassword) { this.truststorePassword = truststorePassword; }
        public int getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
        public int getReadTimeout() { return readTimeout; }
        public void setReadTimeout(int readTimeout) { this.readTimeout = readTimeout; }
    }

    public static class Udp {
        private boolean enabled = false;
        private int port = 8081;
        private int bufferSize = 4096;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public int getBufferSize() { return bufferSize; }
        public void setBufferSize(int bufferSize) { this.bufferSize = bufferSize; }
    }
}