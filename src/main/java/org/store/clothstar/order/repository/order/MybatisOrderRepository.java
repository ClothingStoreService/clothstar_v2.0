package org.store.clothstar.order.repository.order;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.orderDetail.repository.UpperOrderDetailRepository;

import java.util.Optional;

@Mapper
public interface MybatisOrderRepository extends UpperOrderDetailRepository {

    Optional<Order> getOrder(Long orderId);

    int saveOrder(Order order);

    void deliveredToConfirmOrder(Long orderId);

    void updateOrderPrices(Order order);
}
