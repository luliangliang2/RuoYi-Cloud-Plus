package org.ssssssss.magicapi.iot.product;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProductModelRepository implements ProductModelRepository {
    private final ConcurrentHashMap<String, ProductModel> models = new ConcurrentHashMap<>();
    public Optional<ProductModel> find(String tenantId, String productId) { return Optional.ofNullable(models.get(tenantId + "/" + productId)); }
    public ProductModel save(ProductModel model) { models.put(model.tenantId() + "/" + model.productId(), model); return model; }
}

