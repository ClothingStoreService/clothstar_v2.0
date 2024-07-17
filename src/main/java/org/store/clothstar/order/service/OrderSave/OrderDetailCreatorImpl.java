package org.store.clothstar.order.service.OrderSave;

import org.springframework.stereotype.Service;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.entity.ProductLineEntity;

@Service
public class OrderDetailCreatorImpl implements OrderDetailCreator {
    @Override
    public OrderDetail createOrderDetail(CreateOrderDetailRequest createOrderDetailRequest, Order order, ProductLineEntity productLineEntity, ProductEntity productEntity) {
        return createOrderDetailRequest.toOrderDetail(order, productLineEntity, productEntity);
    }
}
