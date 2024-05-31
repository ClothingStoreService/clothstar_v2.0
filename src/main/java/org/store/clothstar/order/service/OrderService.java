package org.store.clothstar.order.service;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.AddressMybatisRepository;
import org.store.clothstar.member.repository.MemberMybatisRepository;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.orderDetail.service.OrderDetailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberMybatisRepository memberMybatisRepository;
    private final AddressMybatisRepository addressMybatisRepository;
    private final OrderDetailService orderDetailService;

    @PermitAll
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {

        return orderRepository.getOrder(orderId)
                .map(OrderResponse::fromOrder)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 주문번호입니다."));
    }

    @PermitAll
    @Transactional
    public Long saveOrder(CreateOrderRequest createOrderRequest) {

        Member member = memberMybatisRepository.findById(createOrderRequest.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."));

        Address address = addressMybatisRepository.findById(createOrderRequest.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "배송지 정보를 찾을 수 없습니다."));

        Order order = createOrderRequest.toOrder(member, address);
        orderRepository.saveOrder(order);

        return order.getOrderId();
    }

    @PermitAll
    @Transactional
    public void deliveredToConfirmOrder(Long orderId) {

        Order order = orderRepository.getOrder(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (order.getStatus() != Status.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.");
        }

        orderRepository.deliveredToConfirmOrder(orderId);
    }
}
