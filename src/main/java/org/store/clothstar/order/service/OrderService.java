package org.store.clothstar.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.OrderDetailDTO;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.repository.OrderDetailRepository;
import org.store.clothstar.orderDetail.service.OrderDetailService;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailService orderDetailService;
    private final ProductJPARepository productJPARepository;
    private final ProductLineJPARepository productLineJPARepository;

    public OrderService(
            @Qualifier("jpaOrderRepository") OrderRepository orderRepository
            , @Qualifier("memberJpaRepository") MemberRepository memberRepository
            , @Qualifier("addressJpaRepository") AddressRepository addressRepository
            , OrderDetailService orderDetailService
            , OrderDetailRepository orderDetailRepository,
            ProductJPARepository productJPARepository, ProductLineJPARepository productLineJPARepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.addressRepository = addressRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailService = orderDetailService;
        this.productJPARepository = productJPARepository;
        this.productLineJPARepository = productLineJPARepository;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"));

        // 주문자 정보 조회
        MemberEntity memberEntity = memberRepository.findById(orderEntity.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다"));

        AddressEntity addressEntity = addressRepository.findById(orderEntity.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "배송지를 찾을 수 없습니다"));

        // 주문 Response 객체 생성
        OrderResponse orderResponse = OrderResponse.from(orderEntity,memberEntity,addressEntity);

        // 주문 상세 정보 설정
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        for (OrderDetailEntity orderDetailEntity : orderEntity.getOrderDetails()) {
            ProductEntity productEntity = productJPARepository.findById(orderDetailEntity.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품옵션정보를 찾을 수 없습니다"));
            ProductLineEntity productLineEntity = productLineJPARepository.findById(orderDetailEntity.getProductLineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"));

            OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                    .orderDetailId(orderDetailEntity.getOrderDetailId())
                    .productName(productLineEntity.getName())
                    .optionName(productEntity.getName())
                    .brandName(productLineEntity.getSeller().getBrandName())
                    .productPrice(productLineEntity.getPrice())
                    .extraCharge(productEntity.getExtraCharge())
                    .quantity(orderDetailEntity.getQuantity())
                    .totalPrice(orderDetailEntity.getOneKindTotalPrice())
                    .build();

            orderDetailDTOList.add(orderDetailDTO);
        }
        orderResponse.setterOrderDetailList(orderDetailDTOList);

        return orderResponse;

//        return orderRepository.findOrderWithDetails(orderId);
    }

//    public Page<OrderResponse> getAllOrderOffsetPaging(Pageable pageable) {
//        return orderRepository.findAllOffsetPaging(pageable);
//    }
//
//    public Slice<OrderResponse> getAllOrderSlicePaging(Pageable pageable) {
//        return orderRepository.findAllSlicePaging(pageable);
//    }

    @Transactional
    public Long saveOrder(CreateOrderRequest createOrderRequest) {

        Member member = memberRepository.findById(createOrderRequest.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_MEMBER.getMessage()));

        Address address = addressRepository.findById(createOrderRequest.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "배송지 정보를 찾을 수 없습니다."));

        OrderEntity orderEntity = createOrderRequest.toOrderEntity(member, address);
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

    @Transactional
    public void updateDeleteAt(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 번호를 찾을 수 없습니다."));

        if(orderEntity.getDeletedAt() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 주문입니다.");
        }

        List<OrderDetailEntity> orderDetailList = orderDetailRepository.findOrderDetailListByOrderId(orderId);
        orderDetailList.forEach(OrderDetailEntity::updateDeletedAt);

        orderEntity.updateDeletedAt();
    }
}