package org.store.clothstar.productLine.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineResponse;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - ProductLine")
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class ProductLineServiceTest {

    @InjectMocks
    private ProductLineService productLineService;

    @Mock
    private ProductLineJPARepository productLineRepository;

    @Mock
    private CategoryJpaRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @DisplayName("상품 리스트 조회에 성공한다.")
    @Test
    public void givenProductLines_whenGetProductLineList_thenGetProductLines() {
        // given
        ProductLineEntity productLine1 = mock(ProductLineEntity.class);
        ProductLineEntity productLine2 = mock(ProductLineEntity.class);
        ProductLineEntity productLine3 = mock(ProductLineEntity.class);

        Seller seller1 = mock(Seller.class);
        Seller seller2 = mock(Seller.class);
        Seller seller3 = mock(Seller.class);

        when(productLine1.getProductLineId()).thenReturn(1L);
        when(productLine1.getSeller()).thenReturn(seller1);
        when(seller1.getBrandName()).thenReturn("브랜드1");
        when(productLine1.getName()).thenReturn("오구 키링");
        when(productLine1.getPrice()).thenReturn(13000);
        when(productLine1.getStatus()).thenReturn(ProductLineStatus.COMING_SOON);

        when(productLine2.getProductLineId()).thenReturn(2L);
        when(productLine2.getSeller()).thenReturn(seller2);
        when(seller2.getBrandName()).thenReturn("브랜드2");
        when(productLine2.getName()).thenReturn("오구 바디 필로우");
        when(productLine2.getPrice()).thenReturn(57000);
        when(productLine2.getStatus()).thenReturn(ProductLineStatus.FOR_SALE);

        when(productLine3.getProductLineId()).thenReturn(3L);
        when(productLine3.getSeller()).thenReturn(seller3);
        when(seller3.getBrandName()).thenReturn("브랜드3");
        when(productLine3.getName()).thenReturn("오구 볼펜");
        when(productLine3.getPrice()).thenReturn(7900);
        when(productLine3.getStatus()).thenReturn(ProductLineStatus.SOLD_OUT);

        List<ProductLineEntity> productLines = List.of(productLine1, productLine2, productLine3);
        when(productLineRepository.findByDeletedAtIsNullAndStatusNotIn(any())).thenReturn(productLines);

        // when
        List<ProductLineResponse> response = productLineService.getAllProductLines();

        // then
        verify(productLineRepository, times(1))
                .findByDeletedAtIsNullAndStatusNotIn(any());
        assertThat(response).hasSize(3);
        assertThat(response.get(0).getName()).isEqualTo("오구 키링");
        assertThat(response.get(0).getPrice()).isEqualTo(13000);
        assertThat(response.get(0).getProductLineStatus()).isEqualTo(ProductLineStatus.COMING_SOON);
        assertThat(response.get(0).getBrandName()).isEqualTo("브랜드1");
    }

    @DisplayName("상품 id와 상품과 1:N 관계에 있는 상품 옵션 리스트를 조회한다.")
    @Test
    public void givenProductLineId_whenGetProductLineWithProducts_thenProductLineWithProducts() {
        // given
        Long productLineId = 1L;
        ProductLineWithProductsJPAResponse mockResponse = mock(ProductLineWithProductsJPAResponse.class);
        when(mockResponse.getProductLineId()).thenReturn(productLineId);
        when(mockResponse.getTotalStock()).thenReturn(90L);

        given(productLineRepository.findProductLineWithOptionsById(productLineId)).willReturn(Optional.of(mockResponse));

        // when
        ProductLineWithProductsJPAResponse response = productLineService.getProductLineWithProducts(productLineId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getProductLineId()).isEqualTo(productLineId);
        assertThat(response.getTotalStock()).isEqualTo(90L);
    }

    @DisplayName("유효한 상품 라인 생성 Request가 들어오면 상품 라인 생성에 성공한다.")
    @Test
    public void givenValidCreateProductLineRequest_whenCreateProductLine_thenProductLineCreated() {
        // given
        Long productLineId = 1L;
        Long memberId = 1L; // 하드코딩된 memberId
        CreateProductLineRequest createProductLineRequest = CreateProductLineRequest.builder()
                .categoryId(1L)
                .name("데님 자켓")
                .content("봄에 입기 딱 좋은 데님 소재의 청자켓이에요!")
                .price(19000)
                .status(ProductLineStatus.ON_SALE)
                .build();

        Seller mockSeller = mock(Seller.class);
        CategoryEntity mockCategory = mock(CategoryEntity.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);

        when(mockProductLine.getProductLineId()).thenReturn(productLineId);
        when(sellerRepository.findById(eq(memberId))).thenReturn(Optional.of(mockSeller));
        when(categoryRepository.findById(eq(1L))).thenReturn(Optional.of(mockCategory));
        when(productLineRepository.save(any(ProductLineEntity.class))).thenReturn(mockProductLine);

        // when
        Long responseProductLineId = productLineService.createProductLine(createProductLineRequest);

        // then
        assertThat(responseProductLineId).isEqualTo(productLineId);
        verify(sellerRepository).findById(eq(memberId));
        verify(categoryRepository).findById(eq(1L));
        verify(productLineRepository).save(any(ProductLineEntity.class));
    }

    @DisplayName("유효한 UpdateProductLineRequest가 들어오면 상품 수정에 성공한다.")
    @Test
    public void givenValidUpdateProductLineRequest_whenUpdateProductLine_thenProductLineUpdated() {
        // given
        Long productLineId = 1L;
        UpdateProductLineRequest updateProductLineRequest = UpdateProductLineRequest.builder()
                .name("워싱 데님 데님 자켓 ")
                .content("봄에 입기 딱 좋은 데님 소재의 워싱 빈티지 청자켓이에요! ")
                .price(19000)
                .status(ProductLineStatus.ON_SALE)
                .build();

        ProductLineEntity productLine = mock(ProductLineEntity.class);

        given(productLineRepository.findById(productLineId)).willReturn(Optional.of(productLine));

        // when
        productLineService.updateProductLine(productLineId, updateProductLineRequest);

        // then
        verify(productLineRepository, times(1)).findById(productLineId);
        verify(productLine, times(1)).updateProductLine(updateProductLineRequest);
    }

    @DisplayName("상품 삭제 요청 시 deletedAt이 설정된다.")
    @Test
    public void givenProductLineId_whenDeleteProductLine_thenSetDeletedAt() {
        // given
        Long productLineId = 1L;
        ProductLineEntity productLine = mock(ProductLineEntity.class);

        given(productLineRepository.findById(productLineId)).willReturn(Optional.of(productLine));

        // when
        productLineService.setDeletedAt(productLineId);

        // then
        verify(productLineRepository, times(1)).findById(productLineId);
        verify(productLine, times(1)).delete();
    }
}