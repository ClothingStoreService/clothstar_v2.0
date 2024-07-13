package org.store.clothstar.orderDetail.repository;

import org.store.clothstar.orderDetail.domain.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository {
    Optional<OrderDetail> findById(Long orderDetailId);

    OrderDetail save(OrderDetail orderdetailEntity);

    List<OrderDetail> findOrderDetailListByOrderId(Long orderId);
}
