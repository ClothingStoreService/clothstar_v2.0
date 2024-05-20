package org.store.clothstar.order.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.order.domain.Order;

import java.util.List;

@Mapper
public interface OrderSellerRepository {

    List<Order> SelectWaitingOrders();

    void approveOrder(Long orderId);

    void cancelOrder(Long orderId);
}
