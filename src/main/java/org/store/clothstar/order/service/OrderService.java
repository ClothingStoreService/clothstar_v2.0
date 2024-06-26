package org.store.clothstar.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.service.OrderDetailService;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    public OrderService(
            @Qualifier("jpaOrderRepository") OrderRepository orderRepository
            ,@Qualifier("memberJpaRepository") MemberRepository memberRepository
            ,@Qualifier("addressJpaRepository") AddressRepository addressRepository
            , OrderDetailService orderDetailService
    ) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {

        return orderRepository.findById(orderId)
                .map(OrderResponse::fromOrderEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 주문번호입니다."));
    }

    @Transactional
    public Long saveOrder(CreateOrderRequest createOrderRequest) {

        MemberEntity memberEntity = memberRepository.findById(createOrderRequest.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."));

        AddressEntity addressEntity = addressRepository.findById(createOrderRequest.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "배송지 정보를 찾을 수 없습니다."));

        OrderEntity orderEntity = createOrderRequest.toOrderEntity(memberEntity, addressEntity);
        orderRepository.save(orderEntity);

        return orderEntity.getOrderId();
    }

    @Transactional
    public void deliveredToConfirmOrder(Long orderId) {

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (orderEntity.getStatus() != Status.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.");
        }

        orderRepository.deliveredToConfirmOrder(orderId);
    }
}
