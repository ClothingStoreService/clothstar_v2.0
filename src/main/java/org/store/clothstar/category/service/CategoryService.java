package org.store.clothstar.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.dto.request.CreateCategoryRequest;
import org.store.clothstar.category.dto.request.UpdateCategoryRequest;
import org.store.clothstar.category.dto.response.CategoryDetailResponse;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJpaRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDetailResponse getCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리를 찾을 수 없습니다."));

        return CategoryDetailResponse.from(category);
    }

    @Transactional
    public Long createCategory(CreateCategoryRequest createCategoryRequest) {
        CategoryEntity category = createCategoryRequest.toCategoryEntity();
        CategoryEntity savedCategory = categoryRepository.save(category);

        return savedCategory.getCategoryId();
    }

    @Transactional
    public void updateCategory(Long categoryId, UpdateCategoryRequest updateProductRequest) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리를 찾을 수 없습니다."));

        category.updateCategory(updateProductRequest);
    }
}
