package org.store.clothstar.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.dto.request.CreateCategoryRequest;
import org.store.clothstar.category.dto.request.UpdateCategoryRequest;
import org.store.clothstar.category.dto.response.CategoryDetailResponse;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.category.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.selectAllCategory().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDetailResponse getCategory(Long categoryId) {
        Category category = categoryRepository.selectCategoryById(categoryId);
        return CategoryDetailResponse.from(category);
    }

    @Transactional
    public Long createCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = createCategoryRequest.toCategory();
        categoryRepository.save(category);
        return category.getCategoryId();
    }

    @Transactional
    public void updateCategory(Long categoryId, UpdateCategoryRequest updateProductRequest) {
        Category category = categoryRepository.selectCategoryById(categoryId);
        category.updateCategory(updateProductRequest);

        categoryRepository.updateCategory(category);
    }
}
