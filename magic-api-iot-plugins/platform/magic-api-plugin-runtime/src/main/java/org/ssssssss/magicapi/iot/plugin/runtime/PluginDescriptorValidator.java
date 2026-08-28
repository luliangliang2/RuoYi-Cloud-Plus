package org.ssssssss.magicapi.iot.plugin.runtime;

import org.ssssssss.magicapi.iot.plugin.api.PluginDescriptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PluginDescriptorValidator {

    public void validate(List<PluginDescriptor> descriptors) {
        Set<String> pluginIds = new HashSet<>();
        for (PluginDescriptor descriptor : descriptors) {
            if (descriptor.id() == null || descriptor.id().isBlank()) {
                throw new PluginRuntimeException("Plugin id must not be blank");
            }
            if (!pluginIds.add(descriptor.id())) {
                throw new PluginRuntimeException("Duplicate plugin id: " + descriptor.id());
            }
        }
        for (PluginDescriptor descriptor : descriptors) {
            for (String dependency : descriptor.requires()) {
                if (!pluginIds.contains(dependency)) {
                    throw new PluginRuntimeException(
                        "Plugin " + descriptor.id() + " requires missing plugin " + dependency);
                }
            }
        }
    }
}
