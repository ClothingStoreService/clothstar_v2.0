package org.store.clothstar.order.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.order.domain.type.ApprovalStatus;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderSellerRequest;
import org.store.clothstar.order.repository.order.MybatisOrderRepository;
import org.store.clothstar.order.repository.orderSeller.UpperOrderSellerRepository;
import org.store.clothstar.orderDetail.repository.UpperOrderDetailRepository;
import org.store.clothstar.orderDetail.service.OrderDetailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderSellerService {

    private final UpperOrderSellerRepository upperOrderSellerRepository;
    private final UpperOrderDetailRepository upperOrderDetailRepository;
    private final MybatisOrderRepository orderRepository;
    private final OrderDetailService orderDetailService;

    public OrderSellerService(
            @Qualifier("jpaOrderSellerRepositoryAdapter") UpperOrderSellerRepository upperOrderSellerRepository,
            @Qualifier("jpaOrderDetailRepositoryAdapter") UpperOrderDetailRepository upperOrderDetailRepository
//            @Qualifier("mybatisOrderSellerRepository") UpperOrderSellerRepository upperOrderSellerRepository
//            @Qualifier("mybatisOrderDetailRepository") UpperOrderDetailRepository upperOrderDetailRepository,
            ,MybatisOrderRepository orderRepository
            ,OrderDetailService orderDetailService
    ) {
        this.upperOrderSellerRepository = upperOrderSellerRepository;
        this.upperOrderDetailRepository = upperOrderDetailRepository;
        this.orderRepository = orderRepository;
        this.orderDetailService=orderDetailService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getWaitingOrder() {

        return upperOrderSellerRepository.SelectWaitingOrders().stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDTO cancelOrApproveOrder(Long orderId, OrderSellerRequest orderSellerRequest) {

        // 주문 유효성 검사
        orderRepository.getOrder(orderId)
                .filter(Order -> Order.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        return processOrder(orderId, orderSellerRequest);
    }

    // 주문 처리
    @Transactional
    public MessageDTO processOrder(Long orderId, OrderSellerRequest orderSellerRequest) {

        MessageDTO messageDTO = null;

        if (orderSellerRequest.getApprovalStatus() == ApprovalStatus.APPROVE) {
            upperOrderSellerRepository.approveOrder(orderId);
            messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 승인 되었습니다.");
        } else if (orderSellerRequest.getApprovalStatus() == ApprovalStatus.CANCEL) {
            upperOrderSellerRepository.cancelOrder(orderId);
            orderDetailService.restoreStockByOrder(orderId);
            messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소 되었습니다.");
        }

        orderRepository.getOrder(orderId)
                .map(OrderResponse::fromOrder)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "처리 후 주문 정보를 찾을 수 없습니다."));

        return messageDTO;
    }
}

