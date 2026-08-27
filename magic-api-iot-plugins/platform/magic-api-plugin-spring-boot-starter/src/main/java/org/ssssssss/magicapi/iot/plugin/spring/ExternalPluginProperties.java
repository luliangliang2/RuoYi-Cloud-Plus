package org.ssssssss.magicapi.iot.plugin.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties("iot.plugins.external")
public class ExternalPluginProperties {
    private boolean enabled = true;
    private Path directory = Path.of("plugins");
    private Path dataDirectory = Path.of("data", "iot-plugins");

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Path getDirectory() { return directory; }
    public void setDirectory(Path directory) { this.directory = directory; }
    public Path getDataDirectory() { return dataDirectory; }
    public void setDataDirectory(Path dataDirectory) { this.dataDirectory = dataDirectory; }
}
