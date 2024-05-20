package org.store.clothstar.category.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.category.domain.Category;

import java.util.List;

@Mapper
public interface CategoryRepository {
    List<Category> selectAllCategory();

    Category selectCategoryById(Long categoryId);

    int save(Category category);

    int updateCategory(Category category);
}