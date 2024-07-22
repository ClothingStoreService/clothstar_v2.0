package org.store.clothstar.order.service.OrderSave;

import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.product.domain.Product;

public interface OrderDetailValidator {
    void validateOrderDetail(CreateOrderDetailRequest request, Product product);
}
