package org.store.clothstar.orderDetail.repository;

import org.store.clothstar.orderDetail.domain.OrderDetail;

import java.util.List;

public interface UpperOrderDetailRepository {

    void saveOrderDetail(OrderDetail orderdetail);

    List<OrderDetail> findByOrderId(Long orderId);
}
