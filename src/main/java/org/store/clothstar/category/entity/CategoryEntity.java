package org.store.clothstar.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.category.dto.request.UpdateCategoryRequest;

@Entity(name = "category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String categoryType;

    public void updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        this.categoryType = updateCategoryRequest.getCategoryType();
    }
}
