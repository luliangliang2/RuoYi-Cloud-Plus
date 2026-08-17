package org.ssssssss.magicapi.iot.product;

import java.util.List;
import java.util.Map;

public record ProductModel(String tenantId, String productId, String name, String protocolId,
                           Map<String, PropertyDefinition> properties, List<String> events,
                           List<String> services, long version) {
    public ProductModel {
        properties = properties == null ? Map.of() : Map.copyOf(properties);
        events = events == null ? List.of() : List.copyOf(events);
        services = services == null ? List.of() : List.copyOf(services);
    }

    public record PropertyDefinition(String identifier, DataType type, boolean required, String unit) {}
    public enum DataType { BOOLEAN, INTEGER, DECIMAL, STRING, DATE_TIME, OBJECT, ARRAY }
}

