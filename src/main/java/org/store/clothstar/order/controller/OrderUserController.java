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
import org.store.clothstar.common.dto.SaveResponseDTO;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.service.OrderService;

@Tag(name = "Order", description = "주문(Order) 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderUserController {

    private final OrderService orderService;

    @Operation(summary = "단일 주문 조회", description = "단일 주문의 정보를 조회한다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(summary = "전체 주문 조회 offset 페이징", description = "전체 주문 리스트를 offset 페이징 형식으로 가져온다.")
    @GetMapping("/offset")
    public ResponseEntity<Page<OrderResponse>> getAllOrderOffsetPaging(
            @PageableDefault(size = 15) Pageable pageable) {
        Page<OrderResponse> orderPages = orderService.getAllOrderOffsetPaging(pageable);
        return ResponseEntity.ok(orderPages);
    }

    @Operation(summary = "전체 주문 조회 slice 페이징", description = "전체 주문 리스트를 slice 페이징 형식으로 가져온다.")
    @GetMapping("/slice")
    public ResponseEntity<Slice<OrderResponse>> getAllOrderSlicePaging(
            @PageableDefault(size = 15) Pageable pageable) {
        Slice<OrderResponse> orderPages = orderService.getAllOrderSlicePaging(pageable);
        return ResponseEntity.ok(orderPages);
    }

    @Operation(summary = "주문 생성", description = "단일 주문을 생성한다.")
    @PostMapping
    public ResponseEntity<SaveResponseDTO> saveOrder(@RequestBody @Validated OrderRequestWrapper orderRequestWrapper) {
        Long orderId = orderService.saveOrder(orderRequestWrapper);
        return ResponseEntity.ok(new SaveResponseDTO(
                orderId, HttpStatus.OK.value(), "주문이 정상적으로 생성되었습니다."));
    }

    @Operation(summary = "(구매자)구매 확정", description = "구매자가 구매 확정 시, 주문상태가 '구매확정'으로 변경된다(단, 주문상태가 '배송완료'일 때만 가능).")
    @PatchMapping("{orderId}/confirm")
    public ResponseEntity<MessageDTO> confirmOrder(@PathVariable Long orderId) {
        orderService.confirmOrder(orderId);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 구매 확정 되었습니다."));
    }

    @Operation(summary = "(구매자)주문 취소", description = "구매자가 주문 취소 시, 주문상태가 '주문취소'로 변경된다(단, 주문상태가 '승인대기' 또는 '주문승인'일 때만 가능).")
    @PatchMapping("{orderId}/cancel")
    public ResponseEntity<MessageDTO> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소되었습니다."));
    }

    @Operation(summary = "주문 삭제", description = "주문 삭제시간을 현재시간으로 업데이트 한다.")
    @DeleteMapping("{orderId}")
    public ResponseEntity<MessageDTO> deleteOrder(@PathVariable Long orderId) {
        orderService.updateDeleteAt(orderId);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 삭제되었습니다."));
    }
}



