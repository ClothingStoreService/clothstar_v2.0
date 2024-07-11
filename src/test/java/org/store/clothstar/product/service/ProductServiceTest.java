package org.store.clothstar.product.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.dto.request.UpdateProductRequest;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;
import org.store.clothstar.productLine.service.ProductLineService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("비즈니스 로직 - Product")
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    ProductLineService productLineService;

    @Mock
    private ProductJPARepository productRepository;

    @Mock
    private ProductLineJPARepository productLineRepository;

    @Mock
    private CategoryJpaRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @DisplayName("product_id로 상품 옵션 단건 조회에 성공한다.")
    @Test
    public void givenProductId_whenGetProductById_thenProductReturned() {
        // given
        Long productId = 1L;
        ProductLineEntity productLine = mock(ProductLineEntity.class);
        when(productLine.getProductLineId()).thenReturn(1L);

        ProductEntity product = ProductEntity.builder()
                .productId(productId)
                .productLine(productLine)
                .name("곰돌이 블랙")
                .extraCharge(1000)
                .stock(30L)
                .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        ProductResponse response = productService.getProduct(productId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(productId);
        assertThat(response.getProductLineId()).isEqualTo(productLine.getProductLineId());
        assertThat(response.getName()).isEqualTo("곰돌이 블랙");
        assertThat(response.getExtraCharge()).isEqualTo(1000);
        assertThat(response.getStock()).isEqualTo(30L);
    }

    @DisplayName("존재하지 않는 product_id로 상품 옵션 조회 시 예외가 발생한다")
    @Test
    public void givenNonExistentProductId_whenGetProduct_thenThrowException() {
        // given
        Long nonExistentProductId = 999L;
        given(productRepository.findById(nonExistentProductId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProduct(nonExistentProductId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("상품 옵션 정보를 찾을 수 없습니다");
    }

    @DisplayName("유효한 상품 생성 Request가 들어오면 상품 생성에 성공한다.")
    @Test
    public void givenValidCreateProductRequest_whenCreateProduct_thenProductCreated() {
        // given
        Long expectedProductId = 1L;
        Long productLineId = 1L;
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .productLineId(productLineId)
                .name("데님 자켓 - 블랙")
                .extraCharge(1000)
                .stock(100L)
                .build();

        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        when(mockProduct.getProductId()).thenReturn(expectedProductId);

        when(productLineRepository.findById(eq(productLineId))).thenReturn(Optional.of(mockProductLine));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(mockProduct);

        // when
        Long actualProductId = productService.createProduct(createProductRequest);

        // then
        assertThat(actualProductId).isEqualTo(expectedProductId);
        verify(productLineRepository).findById(eq(productLineId));
        verify(productRepository).save(any(ProductEntity.class));
    }

    @DisplayName("유효한 productId와 UpdateProductRequest 가 들어오면 product 수정에 성공한다.")
    @Test
    void givenValidProductIdWithUpdateProductRequest_whenUpdateProduct_thenUpdateProductSuccess() {
        // given
        Long productId = 1L;
        ProductEntity product = ProductEntity.builder()
                .productId(productId)
                .name("곰돌이 블랙")
                .extraCharge(1000)
                .stock(30L)
                .build();

        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .name("곰돌이 블랙진")
                .extraCharge(1000)
                .stock(180L)
                .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.updateProduct(productId, updateProductRequest);

        // then
        verify(productRepository).findById(productId);
        assertThat(product.getName()).isEqualTo("곰돌이 블랙진");
        assertThat(product.getExtraCharge()).isEqualTo(1000);
        assertThat(product.getStock()).isEqualTo(180L);
    }
}