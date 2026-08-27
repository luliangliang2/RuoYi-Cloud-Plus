package org.ssssssss.magicapi.iot.plugin.runtime;

import java.util.List;
import java.util.Map;

public interface CapabilityRegistry {

    void register(String pluginId, List<String> capabilities);

    void unregister(String pluginId);

    List<String> providers(String capability);

    Map<String, List<String>> capabilities();
}
