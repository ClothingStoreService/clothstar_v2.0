package org.store.clothstar.category.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.category.entity.CategoryEntity;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {

    @Schema(description = "카테고리 타입(이름)", nullable = false)
    @NotBlank(message = "카테고리 타입을 입력해주세요.")
    private String categoryType;

    public CategoryEntity toCategoryEntity() {
        return CategoryEntity.builder()
                .categoryType(categoryType)
                .build();
    }
}
