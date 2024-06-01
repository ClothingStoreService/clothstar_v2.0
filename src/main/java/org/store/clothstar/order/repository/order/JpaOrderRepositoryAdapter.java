package org.store.clothstar.order.repository.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.AddressJpaRepository;
import org.store.clothstar.member.repository.MemberJpaRepository;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.Optional;

@Slf4j
@Repository
public class JpaOrderRepositoryAdapter implements UpperOrderRepository {
    MemberJpaRepository memberJpaRepository;
    AddressJpaRepository addressJpaRepository;
    JpaOrderRepository jpaOrderRepository;

    JpaOrderRepositoryAdapter(JpaOrderRepository jpaOrderRepository
            ,MemberJpaRepository memberJpaRepository
            ,AddressJpaRepository addressJpaRepository) {
        this.jpaOrderRepository=jpaOrderRepository;
        this.memberJpaRepository=memberJpaRepository;
        this.addressJpaRepository=addressJpaRepository;
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return jpaOrderRepository.findById(orderId)
                .map(Order::new);
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
        MemberEntity member = memberJpaRepository.findById(order.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + order.getMemberId()));

        AddressEntity address = addressJpaRepository.findById(order.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "배송지 정보를 찾을 수 없습니다."));

        return OrderEntity.builder()
                    .orderId(order.getOrderId())
                    .member(member)
                    .address(address)
                    .createdAt(order.getCreatedAt())
                    .status(order.getStatus())
                    .totalShippingPrice(order.getTotalShippingPrice())
                    .totalProductsPrice(order.getTotalProductsPrice())
                    .paymentMethod(order.getPaymentMethod())
                    .totalPaymentPrice(order.getTotalPaymentPrice())
                    .build();
        }

    }
