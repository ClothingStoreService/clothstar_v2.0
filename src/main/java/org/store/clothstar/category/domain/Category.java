package org.store.clothstar.category.domain;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.category.dto.request.UpdateCategoryRequest;

@Getter
@Builder
public class Category {
    private Long categoryId;
    private String categoryType;

    public void updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        this.categoryType = updateCategoryRequest.getCategoryType();
    }
}
