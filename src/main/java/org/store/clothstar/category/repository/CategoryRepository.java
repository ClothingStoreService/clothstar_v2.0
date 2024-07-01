package org.store.clothstar.category.repository;


import org.store.clothstar.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> selectAllCategory();

    Optional<Category> selectCategoryById(Long categoryId);

    int save(Category category);

    int updateCategory(Category category);
}
