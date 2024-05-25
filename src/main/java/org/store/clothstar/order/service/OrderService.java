package org.store.clothstar.order.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.OrderJpaRepository;
import org.store.clothstar.orderDetail.service.OrderDetailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final OrderDetailService orderDetailService;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {

        return orderJpaRepository.findById(orderId)
                .map(order -> OrderResponse.fromOrder(order))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 주문번호입니다."));
    }

    @Transactional
    public Long saveOrder(CreateOrderRequest createOrderRequest) {

        Member member = memberRepository.findById(createOrderRequest.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."));

        Address address = addressRepository.findById(createOrderRequest.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "배송지 정보를 찾을 수 없습니다."));

        OrderEntity orders = createOrderRequest.toOrder(member, address);
        orderJpaRepository.save(orders);
        entityManager.flush();
        entityManager.clear();

        return orders.getOrderId();
    }

    @Transactional
    public void deliveredToConfirmOrder(Long orderId) {

        OrderEntity order = orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (order.getStatus() != Status.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.");
        }

        orderJpaRepository.save(order);
    }
}
