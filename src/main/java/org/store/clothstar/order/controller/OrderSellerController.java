package org.store.clothstar.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderSellerRequest;
import org.store.clothstar.order.service.OrderSellerService;

import java.util.List;

@Tag(name = "OrderSeller", description = "판매자(OrderSeller)의 주문 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/seller/orders")
public class OrderSellerController {

    private final OrderSellerService orderSellerService;

    @Operation(summary = "(판매자) WAITING 주문 리스트 조회", description = "(판매자) 주문상태가 '승인대기'인 주문 리스트를 조회한다.")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getWaitingOrder() {
        List<OrderResponse> orderResponseList = orderSellerService.getWaitingOrder();
        return ResponseEntity.ok(orderResponseList);
    }

    @Operation(summary = "(판매자) 주문 승인 또는 취소", description = "(판매자) 주문을 승인 또는 취소한다.")
    @PatchMapping("/{orderId}")
    public ResponseEntity<MessageDTO> cancelOrApproveOrder(@PathVariable Long orderId,
                                                           @RequestBody @Validated OrderSellerRequest orderSellerRequest) {
        MessageDTO messageDTO = orderSellerService.cancelOrApproveOrder(orderId, orderSellerRequest);
        return ResponseEntity.ok(messageDTO);
    }
}
