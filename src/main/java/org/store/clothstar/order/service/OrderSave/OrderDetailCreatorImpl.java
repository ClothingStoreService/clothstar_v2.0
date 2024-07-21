package org.store.clothstar.order.service.OrderSave;

import org.springframework.stereotype.Service;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.ProductLine;

@Service
public class OrderDetailCreatorImpl implements OrderDetailCreator {
    @Override
    public OrderDetail createOrderDetail(CreateOrderDetailRequest createOrderDetailRequest, Order order, ProductLine productLineEntity, Product productEntity) {
        return createOrderDetailRequest.toOrderDetail(order, productLineEntity, productEntity);
    }
}
