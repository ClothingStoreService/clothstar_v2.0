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
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.service.OrderDetailService;
import org.store.clothstar.order.service.OrderService;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

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

    private static final String ORDER_URL = "/v1/orders";
    private Order order;

    @DisplayName("단일 주문 조회 통합테스트")
    @Test
    public void testGetOrder() throws Exception {
        //given
        Member member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO());
        Address address = addressRepository.save(getCreateAddressRequest().toAddress(member));
        CategoryEntity category = categoryJpaRepository.save(getCreateCategoryRequest().toCategoryEntity());
        Seller seller = sellerRepository.save(new Seller(getCreateSellerRequest(),member));
        ProductLineEntity productLine = productLineJPARepository.save(getCreateProductLineRequest().toProductLineEntity(seller,category));
        ProductEntity product = productJPARepository.save(getCreateProductRequest().toProductEntity(productLine));
        OrderDetail orderDetail = orderDetailRepository.save(getCreateOrderDetailRequest().toOrderDetail(order,productLine,product));

        order = orderUserRepository.save(getCreateOrderRequest().toOrder(member,address));
        order.addOrderDetail(orderDetail);

        Long orderId = order.getOrderId();
        String getOrderURL = ORDER_URL + "/" + orderId;

        //when
        ResultActions actions = mockMvc.perform(get(getOrderURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId));
    }

//    @Test
//    public void testGetAllOrderOffsetPaging() throws Exception {
//        mockMvc.perform(get("/v1/orders/offset")
//                        .param("page", "0")
//                        .param("size", "15"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray());
//    }
//
//    @Test
//    public void testGetAllOrderSlicePaging() throws Exception {
//        mockMvc.perform(get("/v1/orders/slice")
//                        .param("page", "0")
//                        .param("size", "15"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray());
//    }
//
//    @Test
//    public void testSaveOrder() throws Exception {
//        OrderRequestWrapper orderRequestWrapper = new OrderRequestWrapper();
//        // populate orderRequestWrapper with necessary data
//
//        mockMvc.perform(post("/v1/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestWrapper)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("주문이 정상적으로 생성되었습니다."));
//    }
//
//    @Test
//    public void testConfirmOrder() throws Exception {
//        mockMvc.perform(patch("/v1/orders/{orderId}/confirm", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("주문이 정상적으로 구매 확정 되었습니다."));
//    }
//
//    @Test
//    public void testCancelOrder() throws Exception {
//        mockMvc.perform(patch("/v1/orders/{orderId}/cancel", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("주문이 정상적으로 취소되었습니다."));
//    }
//
//    @Test
//    public void testDeleteOrder() throws Exception {
//        mockMvc.perform(delete("/v1/orders/{orderId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("주문이 정상적으로 삭제되었습니다."));
//    }



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
}

