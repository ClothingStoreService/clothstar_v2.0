package org.store.clothstar.order.service.OrderSave;

import org.store.clothstar.product.entity.ProductEntity;

public interface StockUpdater {
    void updateStock(ProductEntity product, int quantity);
}
