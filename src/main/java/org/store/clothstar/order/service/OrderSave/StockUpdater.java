package org.store.clothstar.order.service.OrderSave;


import org.store.clothstar.product.domain.Product;

public interface StockUpdater {
    void updateStock(Product product, int quantity);
}
