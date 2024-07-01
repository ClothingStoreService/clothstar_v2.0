package org.store.clothstar.category.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.entity.CategoryEntity;

@Getter
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String categoryType;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryType(category.getCategoryType())
                .build();
    }

    public static CategoryResponse from(CategoryEntity category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryType(category.getCategoryType())
                .build();
    }
}
