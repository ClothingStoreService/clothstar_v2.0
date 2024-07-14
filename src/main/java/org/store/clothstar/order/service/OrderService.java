package org.store.clothstar.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.vo.OrderDetailDTO;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.service.ProductLineService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderUserRepository orderUserRepository;
    private final MemberService memberService;
    private final AddressService addressService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final ProductLineService productLineService;

    public OrderService(
            OrderUserRepository orderUserRepository
            , MemberService memberService, AddressService addressService
            , OrderDetailService orderDetailService, OrderDetailRepository orderDetailRepository,
            ProductService productService, ProductLineService productLineService) {
        this.orderUserRepository = orderUserRepository;
        this.memberService = memberService;
        this.addressService = addressService;
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.productLineService = productLineService;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        Order order = orderUserRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"));

        Member member = memberService.getMemberByMemberId(order.getMemberId());
        Address address = addressService.getAddressById(order.getAddressId());

        OrderResponse orderResponse = OrderResponse.from(order,member,address);

        List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getDeletedAt() == null)
                .toList();
        List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());

        List<ProductEntity> products = productService.findByIdIn(productIds);
        List<ProductLineEntity> productLines = productLineService.findByIdIn(productLineIds);

        Map<Long, ProductEntity> productMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, product -> product));
        Map<Long, ProductLineEntity> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLineEntity::getId, productLine -> productLine));

        List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
            ProductEntity productEntity = productMap.get(orderDetail.getProductId());
            ProductLineEntity productLineEntity = productLineMap.get(orderDetail.getProductLineId());
            return OrderDetailDTO.from(orderDetail, productEntity, productLineEntity);
        }).collect(Collectors.toList());

        orderResponse.setterOrderDetailList(orderDetailDTOList);

        return orderResponse;
    }

    public Page<OrderResponse> getAllOrderOffsetPaging(Pageable pageable) {
        Page<Order> orderEntities = orderUserRepository.findAll(pageable);

        return orderEntities.map(order -> {
            Member member = memberService.getMemberByMemberId(order.getMemberId());
            Address address = addressService.getAddressById(order.getAddressId());
            OrderResponse orderResponse = OrderResponse.from(order, member, address);

            List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                    .filter(orderDetail -> orderDetail.getDeletedAt() == null)
                    .toList();
            List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
            List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());

            List<ProductEntity> products = productService.findByIdIn(productIds);
            List<ProductLineEntity> productLines = productLineService.findByIdIn(productLineIds);

            Map<Long, ProductEntity> productMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, product -> product));
            Map<Long, ProductLineEntity> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLineEntity::getId, productLine -> productLine));

            List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
                ProductEntity productEntity = productMap.get(orderDetail.getProductId());
                ProductLineEntity productLineEntity = productLineMap.get(orderDetail.getProductLineId());
                return OrderDetailDTO.from(orderDetail, productEntity, productLineEntity);
            }).collect(Collectors.toList());

            orderResponse.setterOrderDetailList(orderDetailDTOList);

            return orderResponse;
        });
    }

    public Slice<OrderResponse> getAllOrderSlicePaging(Pageable pageable) {
        Slice<Order> orderEntities = orderUserRepository.findAll(pageable);

        return orderEntities.map(order -> {
            Member member = memberService.getMemberByMemberId(order.getMemberId());
            Address address = addressService.getAddressById(order.getAddressId());
            OrderResponse orderResponse = OrderResponse.from(order, member, address);

            List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                    .filter(orderDetail -> orderDetail.getDeletedAt() == null)
                    .toList();
            List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
            List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());

            List<ProductEntity> products = productService.findByIdIn(productIds);
            List<ProductLineEntity> productLines = productLineService.findByIdIn(productLineIds);

            Map<Long, ProductEntity> productMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, product -> product));
            Map<Long, ProductLineEntity> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLineEntity::getId, productLine -> productLine));

            List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
                ProductEntity productEntity = productMap.get(orderDetail.getProductId());
                ProductLineEntity productLineEntity = productLineMap.get(orderDetail.getProductLineId());
                return OrderDetailDTO.from(orderDetail, productEntity, productLineEntity);
            }).collect(Collectors.toList());

            orderResponse.setterOrderDetailList(orderDetailDTOList);

            return orderResponse;
        });
    }

    @Transactional
    public Long saveOrder(OrderRequestWrapper orderRequestWrapper) {
        CreateOrderRequest createOrderRequest = orderRequestWrapper.getCreateOrderRequest();
        CreateOrderDetailRequest createOrderDetailRequest = orderRequestWrapper.getCreateOrderDetailRequest();

        // 주문 생성
        Member member = memberService.getMemberByMemberId(createOrderRequest.getMemberId());
        Address address = addressService.getAddressById(createOrderRequest.getAddressId());

        Order order = createOrderRequest.toOrder(member, address);

        ProductLineEntity productLineEntity = productLineService.findById(createOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"));
        ProductEntity productEntity = productService.findById(createOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션을 찾을 수 없습니다"));

        // 주문상세 생성 유효성 검사
        if (createOrderDetailRequest.getQuantity() > productEntity.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetail orderDetail = createOrderDetailRequest.toOrderDetail(order, productLineEntity, productEntity);
        order.addOrderDetail(orderDetail); // 주문에 주문상세 추가

        // 주문 저장 (orderDetail은 cascade 설정에 의해 자동 저장됨)
        orderUserRepository.save(order);

        // 주문 정보 업데이트
        int newTotalProductsPrice = order.getTotalPrice().getProducts() + orderDetail.getPrice().getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalPrice().getProducts() + order.getTotalPrice().getShipping() + orderDetail.getPrice().getOneKindTotalPrice();

        order.getTotalPrice().updatePrices(newTotalProductsPrice, newTotalPaymentPrice);

        // 주문 수량만큼 상품 재고 차감
        orderDetailService.updateProductStock(productEntity, orderDetail.getQuantity());

        return order.getOrderId();
    }

    @Transactional
    public void confirmOrder(Long orderId) {

        Order order = orderUserRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (order.getStatus() != Status.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.");
        }

        orderUserRepository.confirmOrder(orderId);
    }

    public void cancelOrder(Long orderId) {

        Order order = orderUserRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (order.getStatus() != Status.WAITING && order.getStatus() != Status.APPROVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'승인대기' 또는 '주문승인' 상태가 아니기 때문에 주문을 취소할 수 없습니다.");
        }

        orderUserRepository.cancelOrder(orderId);
    }

    @Transactional
    public void updateDeleteAt(Long orderId) {
        Order order = orderUserRepository.findById(orderId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 번호를 찾을 수 없습니다."));

        if(order.getDeletedAt() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 주문입니다.");
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailListByOrderId(orderId);
        orderDetailList.forEach(OrderDetail::updateDeletedAt);

        order.updateDeletedAt();
    }

}