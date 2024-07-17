package org.store.clothstar.order.service.OrderSave;

import org.springframework.stereotype.Service;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.service.ProductService;

@Service
public class StockUpdaterImpl implements StockUpdater {

    private static ProductService productService;

    public StockUpdaterImpl(ProductService productService){
        this.productService=productService;
    }

    @Override
    public void updateStock(ProductEntity product, int quantity) {
        productService.updateProductStock(product, quantity);
    }
}