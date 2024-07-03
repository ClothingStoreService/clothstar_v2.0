package org.store.clothstar.productLine.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineResponse;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;
import org.store.clothstar.productLine.repository.ProductLineRepository;
import org.store.clothstar.productLine.service.ProductLineService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Disabled
public class ProductLineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private ProductLineRepository productLineRepository;

    private static final String PRODUCT_LINE_URL = "/v1/productLines";


    @Test
    @DisplayName("전체 상품 조회 시 모든 ProductLine이 반환된다.")
    void whenGetAllProductLines_thenAllExistingAndNewProductLinesAreReturned() throws Exception {
        // given
        // 기존 데이터 개수 확인
        int initialCount = productLineRepository.selectAllProductLinesNotDeleted().size();

        // 새로운 ProductLine 추가
        List<Long> newProductLineIds = createSampleProductLines(3);
        int expectedTotalCount = initialCount + 3;

        // when
        ResultActions actions = mockMvc.perform(get(PRODUCT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedTotalCount))
                .andDo(print());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        List<ProductLineResponse> responses = objectMapper.readValue(responseBody, new TypeReference<List<ProductLineResponse>>() {
        });

        assertThat(responses).hasSize(expectedTotalCount);

        // 새로 추가한 ProductLine 확인
        for (Long id : newProductLineIds) {
            ProductLine productLine = productLineRepository.selectByProductLineId(id).orElseThrow();
            ProductLineResponse response = responses.stream()
                    .filter(r -> r.getProductLineStatus().equals(id))
                    .findFirst()
                    .orElseThrow();

            assertThat(response.getProductLineId()).isEqualTo(productLine.getProductLineId());
            assertThat(response.getName()).isEqualTo(productLine.getName());
            assertThat(response.getPrice()).isEqualTo(productLine.getPrice());
            assertThat(response.getContent()).isEqualTo(productLine.getContent());
            assertThat(response.getBrandName()).isEqualTo(productLine.getBrandName());
            assertThat(response.getProductLineStatus()).isEqualTo(productLine.getStatus());
        }

        // 기존 데이터도 포함되어 있는지 확인 (샘플로 첫 번째 항목 확인)
        if (initialCount > 0) {
            ProductLine firstExistingProductLine = productLineRepository.selectAllProductLinesNotDeleted().get(0);
            ProductLineResponse firstResponse = responses.stream()
                    .filter(r -> r.getProductLineStatus().equals(firstExistingProductLine.getProductLineId()))
                    .findFirst()
                    .orElseThrow();

            assertThat(firstResponse.getProductLineStatus()).isEqualTo(firstExistingProductLine.getProductLineId());
            assertThat(firstResponse.getName()).isEqualTo(firstExistingProductLine.getName());
            assertThat(firstResponse.getPrice()).isEqualTo(firstExistingProductLine.getPrice());
            assertThat(firstResponse.getProductLineStatus()).isEqualTo(firstExistingProductLine.getStatus());
        }
    }

    @Test
    @DisplayName("특정 ProductLine ID로 조회 시 해당 ProductLine과 관련 Products가 반환된다.")
    void givenProductLineId_whenGetProductLineWithProducts_thenProductLineWithProductsIsReturned() throws Exception {
        // given
        ProductLine productLine = createSampleProductLine();
        String url = PRODUCT_LINE_URL + "/" + productLine.getProductLineId();

        // when
        ResultActions actions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productLine.getProductLineId()))
                .andDo(print());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        ProductLineWithProductsResponse response = objectMapper.readValue(responseBody, ProductLineWithProductsResponse.class);

        assertThat(response.getProductLineId()).isEqualTo(productLine.getProductLineId());
        assertThat(response.getName()).isEqualTo(productLine.getName());
        assertThat(response.getContent()).isEqualTo(productLine.getContent());
        assertThat(response.getPrice()).isEqualTo(productLine.getPrice());
        assertThat(response.getStatus()).isEqualTo(productLine.getStatus());
        // 여기에 하위 제품(Products) 검증 로직 추가
    }

    @Test
    @DisplayName("새로운 ProductLine 생성 요청 시 정상적으로 생성되고 반환된다.")
    void givenValidProductLineRequest_whenCreateProductLine_thenProductLineIsCreatedAndReturned() throws Exception {
        // given
        CreateProductLineRequest request = new CreateProductLineRequest(
                1L, "New Product", "Description", 10000, ProductLineStatus.FOR_SALE);
        String content = objectMapper.writeValueAsString(request);

        // when
        ResultActions actions = mockMvc.perform(post(PRODUCT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());

        String locationHeader = actions.andReturn().getResponse().getHeader("Location");
        Long productLineId = Long.parseLong(locationHeader.substring(locationHeader.lastIndexOf("/") + 1));

        ProductLine createdProductLine = productLineRepository.selectByProductLineId(productLineId).orElseThrow();

        assertThat(createdProductLine.getProductLineId()).isEqualTo(productLineId);
        assertThat(createdProductLine.getCategoryId()).isEqualTo(request.getCategoryId());
        assertThat(createdProductLine.getName()).isEqualTo(request.getName());
        assertThat(createdProductLine.getContent()).isEqualTo(request.getContent());
        assertThat(createdProductLine.getPrice()).isEqualTo(request.getPrice());
        assertThat(createdProductLine.getStatus()).isEqualTo(request.getStatus());
    }

    @Test
    @DisplayName("기존 ProductLine 수정 요청 시 정상적으로 수정되는지 확인")
    void givenProductLineIdAndUpdateRequest_whenUpdateProductLine_thenProductLineIsUpdated() throws Exception {
        // given
        ProductLine originalProductLine = createSampleProductLine();
        UpdateProductLineRequest request = new UpdateProductLineRequest(
                "Updated Product", "Updated Content", 100, ProductLineStatus.FOR_SALE);
        String content = objectMapper.writeValueAsString(request);
        String url = PRODUCT_LINE_URL + "/" + originalProductLine.getProductLineId();

        // when
        ResultActions actions = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ProductLine updated successfully"))
                .andDo(print());

        ProductLine updatedProductLine = productLineRepository.selectByProductLineId(originalProductLine.getProductLineId()).orElseThrow();

        assertThat(updatedProductLine.getProductLineId()).isEqualTo(originalProductLine.getProductLineId());
        assertThat(updatedProductLine.getName()).isEqualTo(request.getName());
        assertThat(updatedProductLine.getContent()).isEqualTo(request.getContent());
        assertThat(updatedProductLine.getPrice()).isEqualTo(request.getPrice());
        assertThat(updatedProductLine.getStatus()).isEqualTo(request.getStatus());
    }

    @Test
    @DisplayName("ProductLine 삭제 요청 시 정상적으로 삭제(soft delete)되는지 확인")
    void givenProductLineId_whenDeleteProductLine_thenProductLineIsSoftDeleted() throws Exception {
        // given
        ProductLine productLine = createSampleProductLine();
        String url = PRODUCT_LINE_URL + "/" + productLine.getProductLineId();

        // when
        ResultActions actions = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(status().isNoContent());

        ProductLine deletedProductLine = productLineRepository.selectByProductLineId(productLine.getProductLineId()).orElseThrow();
        assertThat(deletedProductLine.getDeletedAt()).isNotNull();
    }

    private ProductLine createSampleProductLine() {
        CreateProductLineRequest request = new CreateProductLineRequest(
                1L, "Sample Product", "Description", 10000, ProductLineStatus.FOR_SALE);
        Long productLineId = productLineService.createProductLine(request);
        return productLineRepository.selectByProductLineId(productLineId).orElseThrow();
    }

    private List<Long> createSampleProductLines(int count) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(createSampleProductLine().getProductLineId());
        }
        return ids;
    }
}
