package org.store.clothstar.category.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.dto.request.CreateCategoryRequest;
import org.store.clothstar.category.dto.response.CategoryDetailResponse;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.category.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("비즈니스 로직 - categoryTest")
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리 리스트 조회에 성공한다.")
    @Test
    void givenCategories_whenGetAllCategories_thenGetAllCategories() {
        //given
        List<Category> categories = new ArrayList<>();
        Category category1 = Category.builder()
                .categoryId(1L)
                .categoryType("OUTER")
                .build();
        Category category2 = Category.builder()
                .categoryId(2L)
                .categoryType("TOP")
                .build();
        Category category3 = Category.builder()
                .categoryId(3L)
                .categoryType("PANTS")
                .build();
        Category category4 = Category.builder()
                .categoryId(4L)
                .categoryType("SKIRT")
                .build();
        Category category5 = Category.builder()
                .categoryId(5L)
                .categoryType("BAG")
                .build();
        Category category6 = Category.builder()
                .categoryId(6L)
                .categoryType("HEADWEAR")
                .build();

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);
        categories.add(category5);
        categories.add(category6);

        BDDMockito.given(categoryRepository.selectAllCategory()).willReturn(categories);

        // when
        List<CategoryResponse> response = categoryService.getAllCategories();

        // then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .selectAllCategory();
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(6);
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

        Category category = Category.builder()
                .categoryId(1L)
                .categoryType("OUTER")
                .build();

        BDDMockito.given(categoryRepository.selectCategoryById(anyLong())).willReturn(category);

        // when
        CategoryDetailResponse response = categoryService.getCategory(categoryId);

        // then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .selectCategoryById(anyLong());
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

        BDDMockito.given(categoryRepository.save(Mockito.any(Category.class))).willReturn(1);

        // when
        Long categoryId = categoryService.createCategory(createCategoryRequest);

        // then
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(Mockito.any(Category.class));
    }

    @DisplayName("중복된 카테고리 타입을 생성하려고 시도할 경우, 카테고리 생성에 실패한다.")
    @Test
    void givenDuplicateCategoryType_whenCreateCategory_thenFailToCreateCategory() {

    }

    @Test
    void updateCategory() {
        // given
        String duplicateCategoryType = "OUTER";
    }

}