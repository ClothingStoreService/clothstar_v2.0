package org.store.clothstar.category.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.category.dto.request.CreateCategoryRequest;
import org.store.clothstar.category.dto.response.CategoryDetailResponse;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("비즈니스 로직 - categoryTest")
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryJpaRepository categoryRepository;

    @DisplayName("카테고리 리스트 조회에 성공한다.")
    @Test
    void givenCategories_whenGetAllCategories_thenGetAllCategories() {
        //given
        List<CategoryEntity> categories = new ArrayList<>();
        categories.add(CategoryEntity.builder().categoryId(1L).categoryType("OUTER").build());
        categories.add(CategoryEntity.builder().categoryId(2L).categoryType("TOP").build());
        categories.add(CategoryEntity.builder().categoryId(3L).categoryType("PANTS").build());
        categories.add(CategoryEntity.builder().categoryId(4L).categoryType("SKIRT").build());
        categories.add(CategoryEntity.builder().categoryId(5L).categoryType("BAG").build());
        categories.add(CategoryEntity.builder().categoryId(6L).categoryType("HEADWEAR").build());

        BDDMockito.given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<CategoryResponse> response = categoryService.getAllCategories();

        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
        assertThat(response).isNotNull();
        assertThat(response).hasSize(6);
        assertThat(response.get(0).getCategoryType()).isEqualTo("OUTER");
        assertThat(response.get(1).getCategoryType()).isEqualTo("TOP");
        assertThat(response.get(2).getCategoryType()).isEqualTo("PANTS");
        assertThat(response.get(3).getCategoryType()).isEqualTo("SKIRT");
        assertThat(response.get(4).getCategoryType()).isEqualTo("BAG");
        assertThat(response.get(5).getCategoryType()).isEqualTo("HEADWEAR");
    }

    @DisplayName("category_id로 카테고리 단건 조회에 성공한다.")
    @Test
    void givenCategoryId_whenCategoryId_thenCategoryReturned() {
        // given
        Long categoryId = 1L;
        CategoryEntity category = CategoryEntity.builder()
                .categoryId(1L)
                .categoryType("OUTER")
                .build();

        BDDMockito.given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        CategoryDetailResponse response = categoryService.getCategory(categoryId);

        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(anyLong());
        assertThat(response).isNotNull();
        assertThat(response.getCategoryId()).isEqualTo(1L);
        assertThat(response.getCategoryType()).isEqualTo("OUTER");
    }

    @DisplayName("유효한 CreateCategoryRequest 가 들어오면 카테고리 생성에 성공한다.")
    @Test
    void givenValidCreateCategoryRequest_whenCreateCategory_thenCategoryCreated() {
        // given
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryType("OUTER")
                .build();

        CategoryEntity savedCategory = CategoryEntity.builder()
                .categoryId(1L)
                .categoryType("OUTER")
                .build();

        BDDMockito.given(categoryRepository.save(Mockito.any(CategoryEntity.class))).willReturn(savedCategory);

        // when
        Long categoryId = categoryService.createCategory(createCategoryRequest);

        // then
        ArgumentCaptor<CategoryEntity> categoryCaptor = ArgumentCaptor.forClass(CategoryEntity.class);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryCaptor.capture());

        CategoryEntity capturedCategory = categoryCaptor.getValue();
        assertThat(categoryId).isEqualTo(1L);
        assertThat(capturedCategory.getCategoryType()).isEqualTo("OUTER");
    }

    @DisplayName("중복된 카테고리 타입을 생성하려고 시도할 경우, 카테고리 생성에 실패한다.")
    @Test
    void givenDuplicateCategoryType_whenCreateCategory_thenFailToCreateCategory() {
        // 이 테스트 케이스는 아직 구현되지 않았습니다.
    }

    @Test
    void updateCategory() {
        // given
        String duplicateCategoryType = "OUTER";
        // 이 테스트 케이스는 아직 구현되지 않았습니다.
    }

}