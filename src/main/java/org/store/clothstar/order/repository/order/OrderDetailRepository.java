package org.store.clothstar.order.repository.order;

import org.store.clothstar.order.domain.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository {
    Optional<OrderDetail> findById(Long orderDetailId);

    OrderDetail save(OrderDetail orderdetailEntity);

    List<OrderDetail> findOrderDetailListByOrderId(Long orderId);
}
