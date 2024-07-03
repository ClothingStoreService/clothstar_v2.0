package org.store.clothstar.product.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.dto.request.UpdateProductRequest;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.repository.ProductMybatisRepository;
import org.store.clothstar.product.repository.ProductRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("비즈니스 로직 - Product")
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productMybatisRepository;

    @DisplayName("product_id로 상품 옵션 단건 조회에 성공한다.")
    @Test
    public void givenProductId_whenGetProductById_thenProductReturned() {
        // given
        Long productId = 1L;

        Product product = Product.builder()
                .productId(1L)
                .productLineId(1L)
                .name("곰돌이 블랙")
                .extraCharge(1000)
                .stock(30L)
                .build();

        given(productMybatisRepository.selectByProductId(anyLong())).willReturn(Optional.ofNullable(product));

        // when
        ProductResponse response = productService.getProduct(productId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getProductLineId()).isEqualTo(1L);
        assertThat(response.getExtraCharge()).isEqualTo(1000);
        assertThat(response.getStock()).isEqualTo(30L);
    }

    @DisplayName("유효한 createProductRequest 가 들어오면, product_line_id로 1:N 관계의 상품 옵션 생성에 성공한다.")
    @Test
    void givenCreateProductRequest_whenCreateProduct_thenCreatedProductReturned() {

        Long productLineId = 1L;

        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .productLineId(productLineId)
                .name("곰돌이 블랙")
                .extraCharge(1000)
                .stock(200L)
                .build();

        given(productMybatisRepository.save(any(Product.class))).willReturn(1);

        // when
        Long createdProductId = productService.createProduct(createProductRequest);

        // then
        verify(productMybatisRepository, times(1))
                .save(any(Product.class));
        assertThat(createdProductId).isNotNull();
    }

    @DisplayName("유효한 productId와 UpdateProductRequest 가 들어오면 product 수정에 성공한다.")
    @Test
    void givenValidProductIdWithUpdateProductRequest_whenUpdateProduct_thenUpdateProductSuccess() {
        Long productId = 1L;

        Product product = Product.builder()
                .productId(1L)
                .productLineId(1L)
                .name("곰돌이 블랙")
                .extraCharge(1000)
                .stock(30L)
                .build();

        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .name("곰돌이 블랙진")
                .extraCharge(1000)
                .stock(180L)
                .build();

        given(productMybatisRepository.selectByProductId(anyLong())).willReturn(Optional.ofNullable(product));
        given(productMybatisRepository.updateProduct(any(Product.class))).willReturn(1);

        // when
        productService.updateProduct(productId, updateProductRequest);

        // then
        verify(productMybatisRepository, times(1))
                .selectByProductId(anyLong());
        verify(productMybatisRepository, times(1))
                .updateProduct(any(Product.class));
    }

    @DisplayName("해당 productId의 product 가 존재하면 삭제에 성공한다.")
    @Test
    void deleteProduct() {
        Long productId = 1L;

        Product product = Product.builder()
                .productId(1L)
                .productLineId(1L)
                .name("곰돌이 블랙")
                .extraCharge(1000)
                .stock(30L)
                .build();

        given(productMybatisRepository.selectByProductId(anyLong())).willReturn(Optional.ofNullable(product));
        given(productMybatisRepository.deleteProduct(anyLong())).willReturn(1);

        // when
        productService.deleteProduct(productId);

        // then
        verify(productMybatisRepository, times(1))
                .selectByProductId(anyLong());
        verify(productMybatisRepository, times(1))
                .deleteProduct(anyLong());
    }
}