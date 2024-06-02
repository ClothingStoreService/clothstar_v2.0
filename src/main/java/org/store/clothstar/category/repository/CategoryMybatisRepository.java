package org.store.clothstar.category.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.category.domain.Category;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CategoryMybatisRepository extends CategoryRepository {
    List<Category> selectAllCategory();

    Optional<Category> selectCategoryById(Long categoryId);

    int save(Category category);

    int updateCategory(Category category);
}