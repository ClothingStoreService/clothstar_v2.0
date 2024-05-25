package org.store.clothstar.order.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.orderDetail.service.OrderDetailService;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final EntityManager entityManager;

    @Transactional
    public Long saveOrderWithTransaction(OrderRequestWrapper orderRequestWrapper) {

        Long orderId = orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());

        orderDetailService.saveOrderDetailWithOrder(orderRequestWrapper.getCreateOrderDetailRequest(), orderId);

        return orderId;
    }
}
