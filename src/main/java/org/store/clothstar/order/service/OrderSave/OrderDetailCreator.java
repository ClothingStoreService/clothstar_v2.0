package org.store.clothstar.order.service.OrderSave;

import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.ProductLine;

public interface OrderDetailCreator {
    OrderDetail createOrderDetail(CreateOrderDetailRequest createOrderDetailRequest, Order order, ProductLine productLineEntity, Product productEntity);
}
