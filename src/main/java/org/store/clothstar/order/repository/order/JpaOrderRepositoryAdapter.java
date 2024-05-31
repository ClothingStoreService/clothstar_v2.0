package org.store.clothstar.order.repository.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

import java.util.Optional;

@Slf4j
@Repository
public class JpaOrderRepositoryAdapter implements UpperOrderRepository {
    JpaOrderRepository jpaOrderRepository;

    JpaOrderRepositoryAdapter(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository=jpaOrderRepository;
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return jpaOrderRepository.findById(orderId).map(Order::new);
    }

    @Override
    public int saveOrder(Order order) {
        OrderEntity orderEntity = convertToOrderEntity(order);
        jpaOrderRepository.save(orderEntity);
        return 1;
    }

    @Override
    public void deliveredToConfirmOrder(Long orderId) {
        jpaOrderRepository.deliveredToConfirmOrder(orderId);
    }

    @Override
    public void updateOrderPrices(Order order) {
        OrderEntity orderEntity = jpaOrderRepository.findById(order.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        orderEntity.setTotalProductsPrice(order.getTotalProductsPrice());
        orderEntity.setTotalPaymentPrice(order.getTotalPaymentPrice());

        jpaOrderRepository.save(orderEntity);
    }

    private OrderEntity convertToOrderEntity(Order order) {
        return OrderEntity.builder()
                .orderId(order.getOrderId())
                .memberId(order.getMemberId())
                .addressId(order.getAddressId())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .totalShippingPrice(order.getTotalShippingPrice())
                .totalProductsPrice(order.getTotalProductsPrice())
                .paymentMethod(order.getPaymentMethod())
                .totalPaymentPrice(order.getTotalPaymentPrice())
                .build();
    }
}
