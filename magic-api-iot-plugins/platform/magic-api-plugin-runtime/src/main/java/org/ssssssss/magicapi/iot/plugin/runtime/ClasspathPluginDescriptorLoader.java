package org.ssssssss.magicapi.iot.plugin.runtime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.plugin.api.PluginDescriptor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClasspathPluginDescriptorLoader {

    public static final String DESCRIPTOR_PATH = "META-INF/iot-plugin.json";

    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<PluginDescriptor> load(ClassLoader classLoader) {
        try {
            Enumeration<URL> resources = classLoader.getResources(DESCRIPTOR_PATH);
            List<PluginDescriptor> descriptors = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                try (var input = resource.openStream()) {
                    descriptors.add(objectMapper.readValue(input, PluginDescriptor.class));
                }
            }
            return List.copyOf(descriptors);
        } catch (IOException exception) {
            throw new PluginRuntimeException("Failed to discover IoT plugin descriptors", exception);
        }
    }
}
