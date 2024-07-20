package org.store.clothstar.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.category.dto.request.CreateCategoryRequest;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.member.util.CreateObject;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.service.OrderSellerService;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderSellerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderSellerService orderSellerService;

    @Autowired
    private OrderUserRepository orderUserRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductJPARepository productJPARepository;

    @Autowired
    private ProductLineJPARepository productLineJPARepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private static final String ORDER_SELLER_URL = "/v1/orders/seller";
    private Order order;

    @DisplayName("(판매자) WAITING 주문 리스트 조회 통합테스트")
    @Test
    public void testGetWaitingOrder() throws Exception {
        //given
        createOrders(10, Status.WAITING);

        //when
        ResultActions actions = mockMvc.perform(get(ORDER_SELLER_URL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @DisplayName("(판매자) 주문 승인 통합테스트")
    @Test
    public void testApproveOrder() throws Exception {
        //given
        createOrderWithStatus(Status.WAITING);
        String approveOrderURL = ORDER_SELLER_URL + "/" + order.getOrderId() + "/approve";

        //when
        ResultActions actions = mockMvc.perform(patch(approveOrderURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 정상적으로 승인 되었습니다."));

        // 데이터베이스에서 주문 상태 조회하여 검증
        Order savedOrder = orderUserRepository.findById(order.getOrderId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals(Status.APPROVE, savedOrder.getStatus());
    }

    @DisplayName("(판매자) 주문 취소 통합테스트")
    @Test
    public void testCancelOrder() throws Exception {
        //given
        createOrderWithStatus(Status.WAITING);
        String cancelOrderURL = ORDER_SELLER_URL + "/" + order.getOrderId() + "/cancel";

        //when
        ResultActions actions = mockMvc.perform(patch(cancelOrderURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 정상적으로 취소 되었습니다."));

        // 데이터베이스에서 주문 상태 조회하여 검증
        Order savedOrder = orderUserRepository.findById(order.getOrderId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals(Status.CANCEL, savedOrder.getStatus());
    }

    public static CreateOrderRequest getCreateOrderRequest() {
        PaymentMethod paymentMethod = PaymentMethod.CARD;
        Long memberId = 1L;
        Long addressId = 1L;

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                paymentMethod, memberId, addressId
        );

        return createOrderRequest;
    }

    private CreateAddressRequest getCreateAddressRequest() {
        final String receiverName = "현수";
        final String zipNo = "18292";
        final String addressBasic = "서울시 노원구 공릉동";
        final String addressDetail = "양지빌라";
        final String telNo = "019-1222-2311";
        final String deliveryRequest = "문앞에 놓고 가주세요";
        final boolean defaultAddress = true;

        CreateAddressRequest createAddressRequest = new CreateAddressRequest(
                receiverName, zipNo, addressBasic, addressDetail, telNo, deliveryRequest, defaultAddress
        );

        return createAddressRequest;
    }

    private CreateProductRequest getCreateProductRequest() {
        Long productLineId = 1L;
        String name = "검정";
        int extraCharge = 0;
        Long stock = 10L;

        CreateProductRequest createProductRequest = new CreateProductRequest(
                productLineId, name, extraCharge, stock
        );

        return createProductRequest;
    }

    private CreateProductLineRequest getCreateProductLineRequest() {
        Long categoryId = 1L;
        String name = "나이키 반팔";
        String content = "나이키 반팔 설명";
        int price = 10000;
        ProductLineStatus productLineStatus = ProductLineStatus.COMING_SOON;

        CreateProductLineRequest createProductLineRequest = new CreateProductLineRequest(
                categoryId, name, content, price, productLineStatus
        );

        return createProductLineRequest;
    }

    private CreateCategoryRequest getCreateCategoryRequest() {
        String categoryType = "상의";

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(
                categoryType
        );

        return createCategoryRequest;
    }

    private CreateSellerRequest getCreateSellerRequest() {
        String brandName = "나이키";
        String bizNo = "102-13-13122";

        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }

    private CreateOrderDetailRequest getCreateOrderDetailRequest() {
        Long productLineId = 1L;
        Long productId = 1L;
        int quantity = 1;

        CreateOrderDetailRequest createOrderDetailRequest = new CreateOrderDetailRequest(
                productLineId, productId, quantity
        );

        return createOrderDetailRequest;
    }

    private void createOrderWithStatus(Status status) {
        Member member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO());
        Address address = addressRepository.save(getCreateAddressRequest().toAddress(member));
        CategoryEntity category = categoryJpaRepository.save(getCreateCategoryRequest().toCategoryEntity());
        Seller seller = sellerRepository.save(new Seller(getCreateSellerRequest(),member));
        ProductLineEntity productLine = productLineJPARepository.save(getCreateProductLineRequest().toProductLineEntity(seller,category));
        ProductEntity product = productJPARepository.save(getCreateProductRequest().toProductEntity(productLine));
        OrderDetail orderDetail = orderDetailRepository.save(getCreateOrderDetailRequest().toOrderDetail(order,productLine,product));

        order = orderUserRepository.save(getCreateOrderRequest().toOrder(member,address));
        order.setterStatus(status);
        order.addOrderDetail(orderDetail);
    }

    private void createOrders(int count, Status status) {
        for (int i = 0; i < count; i++) {
            Member member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO(i));
            Address address = addressRepository.save(getCreateAddressRequest().toAddress(member));
            CategoryEntity category = categoryJpaRepository.save(getCreateCategoryRequest(i).toCategoryEntity()); // 수정된 부분
            Seller seller = sellerRepository.save(new Seller(getCreateSellerRequest(i), member));
            ProductLineEntity productLine = productLineJPARepository.save(getCreateProductLineRequest(category.getCategoryId()).toProductLineEntity(seller, category)); // 수정된 부분
            ProductEntity product = productJPARepository.save(getCreateProductRequest(productLine.getProductLineId()).toProductEntity(productLine)); // 수정된 부분
            OrderDetail orderDetail = orderDetailRepository.save(getCreateOrderDetailRequest(productLine.getProductLineId(), product.getProductId()).toOrderDetail(order, productLine, product)); // 수정된 부분

            order = orderUserRepository.save(getCreateOrderRequest(member.getMemberId(), address.getAddressId()).toOrder(member, address)); // 수정된 부분
            order.setterStatus(status);
            order.addOrderDetail(orderDetail);
        }
    }

    private CreateSellerRequest getCreateSellerRequest(int index) {
        String brandName = "나이키" + index;
        String bizNo = "102-13-1312" + index;

        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }

    private CreateCategoryRequest getCreateCategoryRequest(int index) {
        String categoryType = "상의" + index; // 중복을 피하기 위해 인덱스를 추가

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(
                categoryType
        );

        return createCategoryRequest;
    }

    private CreateProductLineRequest getCreateProductLineRequest(Long categoryId) {
        String name = "나이키 반팔";
        String content = "나이키 반팔 설명";
        int price = 10000;
        ProductLineStatus productLineStatus = ProductLineStatus.COMING_SOON;

        CreateProductLineRequest createProductLineRequest = new CreateProductLineRequest(
                categoryId, name, content, price, productLineStatus
        );

        return createProductLineRequest;
    }

    private CreateProductRequest getCreateProductRequest(Long productLineId) {
        String name = "검정";
        int extraCharge = 0;
        Long stock = 10L;

        CreateProductRequest createProductRequest = new CreateProductRequest(
                productLineId, name, extraCharge, stock
        );

        return createProductRequest;
    }

    private CreateOrderDetailRequest getCreateOrderDetailRequest(Long productLineId, Long productId) {
        int quantity = 1;

        CreateOrderDetailRequest createOrderDetailRequest = new CreateOrderDetailRequest(
                productLineId, productId, quantity
        );

        return createOrderDetailRequest;
    }

    private CreateOrderRequest getCreateOrderRequest(Long memberId, Long addressId) {
        PaymentMethod paymentMethod = PaymentMethod.CARD;

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                paymentMethod, memberId, addressId
        );

        return createOrderRequest;
    }
}

