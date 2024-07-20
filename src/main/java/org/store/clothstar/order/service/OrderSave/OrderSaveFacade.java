package org.store.clothstar.order.service.OrderSave;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.service.ProductLineService;

@Service
public class OrderSaveFacade {

    private final MemberService memberService;
    private final AddressService addressService;
    private final ProductService productService;
    private final ProductLineService productLineService;
    private final OrderCreator orderCreator;
    private final OrderDetailValidator orderDetailValidator;
    private final OrderDetailCreator orderDetailCreator;
    private final OrderDetailAdder orderDetailAdder;
    private final OrderSaver orderSaver;
    private final OrderPriceUpdater orderPriceUpdater;
    private final StockUpdater stockUpdater;


    public OrderSaveFacade(
            MemberService memberService, AddressService addressService
            , ProductService productService, ProductLineService productLineService
            , OrderCreator orderCreator
            , OrderDetailValidator orderDetailValidator
            , OrderDetailCreator orderDetailCreator
            , OrderDetailAdder orderDetailAdder
            , OrderSaver orderSaver
            , OrderPriceUpdater orderPriceUpdater
            , StockUpdater stockUpdater
    ) {
        this.memberService = memberService;
        this.addressService = addressService;
        this.productService = productService;
        this.productLineService = productLineService;
        this.orderCreator=orderCreator;
        this.orderDetailValidator = orderDetailValidator;
        this.orderDetailCreator = orderDetailCreator;
        this.orderDetailAdder=orderDetailAdder;
        this.orderSaver=orderSaver;
        this.orderPriceUpdater=orderPriceUpdater;
        this.stockUpdater=stockUpdater;
    }

    @Transactional
    public Long saveOrder(OrderRequestWrapper orderRequestWrapper) {
        CreateOrderRequest createOrderRequest = orderRequestWrapper.getCreateOrderRequest();
        CreateOrderDetailRequest createOrderDetailRequest = orderRequestWrapper.getCreateOrderDetailRequest();
        Member member = memberService.getMemberByMemberId(createOrderRequest.getMemberId());
        Address address = addressService.getAddressById(createOrderRequest.getAddressId());
        ProductLineEntity productLineEntity = productLineService.findById(createOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"));
        ProductEntity productEntity = productService.findById(createOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션을 찾을 수 없습니다"));

        // 요청 DTO로부터 주문 생성
        Order order = orderCreator.createOrder(createOrderRequest,member,address);

        // 주문상세 생성 유효성 검사
        orderDetailValidator.validateOrderDetail(createOrderDetailRequest,productEntity);

        // 주문상세 생성
        OrderDetail orderDetail = orderDetailCreator.createOrderDetail(createOrderDetailRequest, order, productLineEntity, productEntity);

        // 주문에 주문상세 추가
        orderDetailAdder.addOrderDetail(order, orderDetail);

        // 주문 저장 (orderDetail은 cascade 설정에 의해 자동 저장됨)
        orderSaver.saveOrder(order);

        // 주문 정보 업데이트
        orderPriceUpdater.updateOrderPrice(order,orderDetail);

        // 주문 수량만큼 상품 재고 차감
        stockUpdater.updateStock(productEntity,orderDetail.getQuantity());

        return order.getOrderId();
    }
}
