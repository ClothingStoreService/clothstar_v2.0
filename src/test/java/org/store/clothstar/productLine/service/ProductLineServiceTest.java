package org.store.clothstar.productLine.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineResponse;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;
import org.store.clothstar.productLine.repository.ProductLineMybatisRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - ProductLine")
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class ProductLineServiceTest {

    @InjectMocks
    private ProductLineService productLineService;

    @Mock
    private ProductLineMybatisRepository productLineMybatisRepository;

    @DisplayName("상품 리스트 조회에 성공한다.")
    @Test
    public void givenProductLines_whenGetProductLineList_thenGetProductLines() {
        // given
        ProductLine productLine1 = mock(ProductLine.class);
        ProductLine productLine2 = mock(ProductLine.class);
        ProductLine productLine3 = mock(ProductLine.class);

        when(productLine1.getName()).thenReturn("오구 키링");
        when(productLine1.getPrice()).thenReturn(13000);
        when(productLine1.getStatus()).thenReturn(ProductLineStatus.COMING_SOON);

        when(productLine2.getName()).thenReturn("오구 바디 필로우");
        when(productLine2.getPrice()).thenReturn(57000);
        when(productLine2.getStatus()).thenReturn(ProductLineStatus.FOR_SALE);

        when(productLine3.getName()).thenReturn("오구 볼펜");
        when(productLine3.getPrice()).thenReturn(7900);
        when(productLine3.getStatus()).thenReturn(ProductLineStatus.SOLD_OUT);

        List<ProductLine> productLines = List.of(productLine1, productLine2, productLine3);
        when(productLineMybatisRepository.selectAllProductLinesNotDeleted()).thenReturn(productLines);

        // when
        List<ProductLineResponse> response = productLineService.getAllProductLines();

        // then
        verify(productLineMybatisRepository, times(1))
                .selectAllProductLinesNotDeleted();
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(0).getName()).isEqualTo("오구 키링");
        assertThat(response.get(0).getPrice()).isEqualTo(13000);
//        assertThat(response.get(0).getTotalStock()).isEqualTo(20);
        assertThat(response.get(0).getProductLineStatus()).isEqualTo(ProductLineStatus.COMING_SOON);
    }

    @DisplayName("product_line_id로 상품 단건 조회에 성공한다.")
    @Test
    public void givenProductLineId_whenGetProductLineById_thenProductLineReturned() {
        // given
        ProductLine productLine = mock(ProductLine.class);
        when(productLine.getBrandName()).thenReturn("내셔널지오그래픽키즈 제주점");
        when(productLine.getName()).thenReturn("내셔널지오그래픽 곰돌이 후드티");
        when(productLine.getContent()).thenReturn("귀여운 곰돌이가 그려진 후드티에요!");
        when(productLine.getPrice()).thenReturn(69000);
        when(productLine.getStatus()).thenReturn(ProductLineStatus.ON_SALE);

        given(productLineMybatisRepository.selectByProductLineId(anyLong())).willReturn(Optional.ofNullable(productLine));

        // when
        Optional<ProductLineResponse> response = productLineService.getProductLine(productLine.getProductLineId());

        // then
        assertThat(response).isPresent(); // Optional이 비어있지 않은지 확인

        // Optional이 비어있지 않은 경우에만 값을 가져와서 검증
        response.ifPresent(productLineResponse -> {
            assertThat(productLineResponse.getBrandName()).isEqualTo("내셔널지오그래픽키즈 제주점");
            assertThat(productLineResponse.getName()).isEqualTo("내셔널지오그래픽 곰돌이 후드티");
            assertThat(productLineResponse.getContent()).isEqualTo("귀여운 곰돌이가 그려진 후드티에요!");
            assertThat(productLineResponse.getPrice()).isEqualTo(69000);
            assertThat(productLineResponse.getProductLineStatus()).isEqualTo(ProductLineStatus.ON_SALE);
        });

    }

    @DisplayName("상품 id와 상품과 1:N 관계에 있는 상품 옵션 리스트를 조회한다.")
    @Test
    public void givenProductLineId_whenGetProductLineWithProducts_thenProductLineWithProducts() {
        // given
        Long productLineId = 1L;
        Product product1 = Product.builder()
                .productId(1L)
                .productLineId(1L)
                .name("블랙")
                .extraCharge(0)
                .stock(30L)
                .build();

        Product product2 = Product.builder()
                .productId(2L)
                .productLineId(1L)
                .name("화이트")
                .extraCharge(1000)
                .stock(30L)
                .build();

        Product product3 = Product.builder()
                .productId(3L)
                .productLineId(1L)
                .name("네이비")
                .extraCharge(1000)
                .stock(30L)
                .build();

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);

        ProductLineWithProductsResponse productLineWithProductsResponse = ProductLineWithProductsResponse.builder()
                .productLineId(1L)
                .memberId(1L)
                .categoryId(2L)
                .name("내셔널지오그래픽 곰돌이 후드티")
                .content("귀여운 곰돌이가 그려진 후드티에요!")
                .price(69000)
                .status(ProductLineStatus.ON_SALE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .deletedAt(null)
                .brandName("내셔널지오그래픽키즈 제주점")
                .biz_no("232-05-02861")
                .productList(productList)
                .build();

        given(productLineMybatisRepository.selectProductLineWithOptions(anyLong())).willReturn(Optional.ofNullable(productLineWithProductsResponse));

        // when
        ProductLineWithProductsResponse response = productLineService.getProductLineWithProducts(productLineId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getProductLineId()).isEqualTo(1L);
        assertThat(response.getTotalStock()).isEqualTo(90L);
    }

    @DisplayName("유효한 상품 생성 Request가 들어오면 상품 생성에 성공한다.")
    @Test
    public void givenValidCreateProductLineRequest_whenCreateProductLine_thenProductLineCreated() {
        // given
        Long productLineId = 1L;
        CreateProductLineRequest createProductLineRequest = CreateProductLineRequest.builder()
                .categoryId(1L)
                .name("데님 자켓")
                .content("봄에 입기 딱 좋은 데님 소재의 청자켓이에요!")
                .price(19000)
                .status(ProductLineStatus.ON_SALE)
                .build();

        given(productLineMybatisRepository.save(any(ProductLine.class))).willReturn(1);

        // when
        Long responseProductLineId = productLineService.createProductLine(createProductLineRequest);

        // then
        verify(productLineMybatisRepository, times(1))
                .save(any(ProductLine.class));
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

        ProductLine productLine = ProductLine.builder()
                .productLineId(productLineId)
                .memberId(1L)
                .categoryId(1L)
                .name("데님 자켓")
                .price(19000)
                .totalStock(50L)
                .status(ProductLineStatus.ON_SALE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .deletedAt(null)
                .brandName("내셔널지오그래픽키즈 제주점")
                .biz_no("232-05-02861")
                .build();

        given(productLineMybatisRepository.selectByProductLineId(anyLong())).willReturn(Optional.ofNullable(productLine));
        given(productLineMybatisRepository.updateProductLine(any(ProductLine.class))).willReturn(1);

        // when
        productLineService.updateProductLine(productLineId, updateProductLineRequest);

        // then
        verify(productLineMybatisRepository, times(1))
                .selectByProductLineId(anyLong());
        verify(productLineMybatisRepository, times(1))
                .updateProductLine(any(ProductLine.class));
    }

    @DisplayName("유효한 UpdateProductLineRequest가 들어오면 상품 수정에 성공한다.")
    @Test
    public void givenProductLineId_whenDeleteProducctLine_thenSetDeletedAt() {
        // given
        Long productLineId = 1L;

        ProductLine productLine = ProductLine.builder()
                .productLineId(productLineId)
                .memberId(1L)
                .categoryId(1L)
                .name("데님 자켓")
                .price(19000)
                .totalStock(50L)
                .status(ProductLineStatus.ON_SALE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .deletedAt(null)
                .brandName("내셔널지오그래픽키즈 제주점")
                .biz_no("232-05-02861")
                .build();

        given(productLineMybatisRepository.selectByProductLineId(anyLong())).willReturn(Optional.ofNullable(productLine));
        given(productLineMybatisRepository.setDeletedAt(any(ProductLine.class))).willReturn(1);

        // when
        productLineService.setDeletedAt(productLineId);

        // then
        verify(productLineMybatisRepository, times(2))
                .selectByProductLineId(anyLong());
        verify(productLineMybatisRepository, times(1))
                .setDeletedAt(any(ProductLine.class));
    }
}