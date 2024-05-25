package org.store.clothstar.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.order.domain.type.ApprovalStatus;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderSellerRequest;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.OrderJpaRepository;
import org.store.clothstar.order.repository.OrderRepository;
import org.store.clothstar.order.repository.OrderSellerJpaRepository;
import org.store.clothstar.order.repository.OrderSellerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderSellerService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderSellerJpaRepository orderSellerJpaRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> getWaitingOrder() {

        return orderSellerJpaRepository.findByStatus(Status.WAITING).stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDTO cancelOrApproveOrder(Long orderId, OrderSellerRequest orderSellerRequest) {

        // 주문 유효성 검사
        orderJpaRepository.findById(orderId)
                .filter(o -> o.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        return processOrder(orderId, orderSellerRequest);
    }

    // 주문 처리
    @Transactional
    public MessageDTO processOrder(Long orderId, OrderSellerRequest orderSellerRequest) {

        MessageDTO messageDTO = null;

        Optional<OrderEntity> orderOptional = orderJpaRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            OrderEntity order = orderOptional.get();

            if (orderSellerRequest.getApprovalStatus() == ApprovalStatus.APPROVE) {
                order.updateStatus(Status.APPROVE);
                orderSellerJpaRepository.save(order);
                messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 승인 되었습니다.", null);
            } else if (orderSellerRequest.getApprovalStatus() == ApprovalStatus.CANCEL) {
                order.updateStatus(Status.CANCEL);
                orderSellerJpaRepository.save(order);
                messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소 되었습니다.", null);
            }

            orderJpaRepository.findById(orderId)
                    .map(OrderResponse::fromOrder)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "처리 후 주문 정보를 찾을 수 없습니다."));
        }
        return messageDTO;
    }
}
