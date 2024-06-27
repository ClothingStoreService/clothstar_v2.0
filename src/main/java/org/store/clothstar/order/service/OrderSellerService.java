package org.store.clothstar.order.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.repository.orderSeller.OrderSellerRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.service.OrderDetailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderSellerService {

    private final OrderSellerRepository orderSellerRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;

    public OrderSellerService(
            @Qualifier("jpaOrderSellerRepository") OrderSellerRepository orderSellerRepository,
            @Qualifier("jpaOrderRepository") OrderRepository orderRepository
            ,OrderDetailService orderDetailService
    ) {
        this.orderSellerRepository = orderSellerRepository;
        this.orderRepository = orderRepository;
        this.orderDetailService=orderDetailService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getWaitingOrder() {

        return orderSellerRepository.findWaitingOrders().stream()
                .map(OrderResponse::fromOrderEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDTO approveOrder(Long orderId) {
        MessageDTO messageDTO;

        // 주문 유효성 검사
        orderRepository.findById(orderId)
                .filter(OrderEntity -> OrderEntity.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        orderSellerRepository.approveOrder(orderId);
        messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 승인 되었습니다.");

        orderRepository.findById(orderId)
                .map(OrderResponse::fromOrderEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "처리 후 주문 정보를 찾을 수 없습니다."));

        return messageDTO;
    }

    @Transactional
    public MessageDTO cancelOrder(Long orderId) {
        MessageDTO messageDTO;

        // 주문 유효성 검사
        orderRepository.findById(orderId)
                .filter(Order -> Order.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        orderSellerRepository.cancelOrder(orderId);
        orderDetailService.restoreStockByOrder(orderId);
        messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소 되었습니다.");

        orderRepository.findById(orderId)
                .map(OrderResponse::fromOrderEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "처리 후 주문 정보를 찾을 수 없습니다."));

        return messageDTO;
    }
}

