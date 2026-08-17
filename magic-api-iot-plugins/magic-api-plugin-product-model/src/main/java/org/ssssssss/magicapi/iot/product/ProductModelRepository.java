package org.ssssssss.magicapi.iot.product;

import java.util.Optional;

public interface ProductModelRepository {
    Optional<ProductModel> find(String tenantId, String productId);
    ProductModel save(ProductModel model);
}

