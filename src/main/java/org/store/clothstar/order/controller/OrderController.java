package org.store.clothstar.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.order.dto.reponse.OrderPageResponse;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.service.OrderApplicationService;
import org.store.clothstar.order.service.OrderService;
import org.store.clothstar.order.utils.URIBuilder;

import java.net.URI;



@Tag(name = "Order", description = "주문(Order) 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
//@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderApplicationService orderApplicationService;

    @Operation(summary = "단일 주문 조회", description = "단일 주문의 정보를 조회한다.")
    @GetMapping("/v1/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@Validated @PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(summary = "전체 주문 조회 offset 페이징", description = "전체 주문 리스트를 offset 페이징 형식으로 가져온다.")
    @GetMapping("/v1/orders/list")
    public ResponseEntity<Page<OrderPageResponse>> getAllOrderOffsetPaging(
            @PageableDefault(size = 15) Pageable pageable) {
        Page<OrderPageResponse> orderPages = orderService.getAllOrderOffsetPaging(pageable);
        return ResponseEntity.ok(orderPages);
    }

    @Operation(summary = "전체 주문 조회 slice 페이징", description = "전체 주문 리스트를 slice 페이징 형식으로 가져온다.")
    @GetMapping("/v2/orders/list")
    public ResponseEntity<Slice<OrderPageResponse>> getAllOrderSlicePaging(
            @PageableDefault(size = 15) Pageable pageable) {
        Slice<OrderPageResponse> orderPages = orderService.getAllOrderSlicePaging(pageable);
        return ResponseEntity.ok(orderPages);
    }

    @Operation(summary = "주문 생성", description = "단일 주문을 생성한다.")
    @PostMapping("/v1/orders")
    public ResponseEntity<URI> saveOrder(@RequestBody @Validated OrderRequestWrapper orderRequestWrapper) {
        Long orderId = orderApplicationService.saveOrderWithTransaction(orderRequestWrapper);
        URI location = URIBuilder.buildURI(orderId);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "구매 확정", description = "구매자가 구매 확정 시, 주문상태가 '구매확정'으로 변경된다.")
    @PatchMapping("/v1/orders/{orderId}")
    public ResponseEntity<MessageDTO> deliveredToConfirmOrder(@PathVariable Long orderId) {
        orderService.deliveredToConfirmOrder(orderId);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 구매 확정 되었습니다."));
    }
}



