package org.store.clothstar.order.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.store.clothstar.order.dto.request.AddOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;


public class CreateOrder {

    public static CreateAddressRequest getCreateAddressRequest() {
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

    public static CreateProductLineRequest getCreateProductLineRequest() {
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

    public static CreateCategoryRequest getCreateCategoryRequest() {
        String categoryType = "상의";

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(
                categoryType
        );

        return createCategoryRequest;
    }

    public static CreateSellerRequest getCreateSellerRequest() {
        String brandName = "나이키";
        String bizNo = "102-13-13122";

        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }

//    public static CreateOrderDetailRequest getCreateOrderDetailRequest(Long productLineId, Long productId) {
//        int quantity = 1;
//
//        CreateOrderDetailRequest createOrderDetailRequest = new CreateOrderDetailRequest(
//                productLineId, productId, quantity
//        );
//
//        return createOrderDetailRequest;
//    }

    public static AddOrderDetailRequest getAddOrderDetailRequest(Long orderId, Long productLineId, Long productId) {
        int quantity = 1;

        AddOrderDetailRequest addOrderDetailRequest = new AddOrderDetailRequest(
                orderId, productLineId, productId, quantity
        );

        return addOrderDetailRequest;
    }



    public static CreateSellerRequest getCreateSellerRequest(int index) {
        String brandName = "나이키" + index;
        String bizNo = "102-13-1312" + index;

        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }

    public static CreateCategoryRequest getCreateCategoryRequest(int index) {
        String categoryType = "상의" + index; // 중복을 피하기 위해 인덱스를 추가

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(
                categoryType
        );

        return createCategoryRequest;
    }

    public static CreateProductLineRequest getCreateProductLineRequest(Long categoryId) {
        String name = "나이키 반팔";
        String content = "나이키 반팔 설명";
        int price = 10000;
        ProductLineStatus productLineStatus = ProductLineStatus.COMING_SOON;

        CreateProductLineRequest createProductLineRequest = new CreateProductLineRequest(
                categoryId, name, content, price, productLineStatus
        );

        return createProductLineRequest;
    }

    public static CreateProductRequest getCreateProductRequest(Long productLineId) {
        String name = "검정";
        int extraCharge = 0;
        Long stock = 10L;

        CreateProductRequest createProductRequest = new CreateProductRequest(
                productLineId, name, extraCharge, stock
        );

        return createProductRequest;
    }

    public static CreateOrderDetailRequest getCreateOrderDetailRequest(Long productLineId, Long productId) {
        int quantity = 1;

        CreateOrderDetailRequest createOrderDetailRequest = new CreateOrderDetailRequest(
                productLineId, productId, quantity
        );

        return createOrderDetailRequest;
    }

    public static CreateOrderRequest getCreateOrderRequest(Long memberId, Long addressId) {
        PaymentMethod paymentMethod = PaymentMethod.CARD;

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                paymentMethod, memberId, addressId
        );

        return createOrderRequest;
    }


}
