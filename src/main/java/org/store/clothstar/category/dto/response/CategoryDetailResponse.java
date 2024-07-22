package org.store.clothstar.category.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.category.entity.CategoryEntity;

@Getter
@Builder
public class CategoryDetailResponse {
    private Long categoryId;
    private String categoryType;

    public static CategoryDetailResponse from(CategoryEntity category) {
        return CategoryDetailResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryType(category.getCategoryType())
                .build();
    }

}
