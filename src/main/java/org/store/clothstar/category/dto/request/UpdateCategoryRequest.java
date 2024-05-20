package org.store.clothstar.category.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryRequest {

    @Schema(description = "카테고리 타입(이름)", nullable = false)
    @NotBlank(message = "카테고리 타입을 입력해주세요.")
    private String categoryType;
}
