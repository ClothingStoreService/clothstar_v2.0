package org.store.clothstar.category.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.repository.CategoryMybatisRepository;
import org.store.clothstar.category.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mybatis")
@RequiredArgsConstructor
public class MybatisCategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryMybatisRepository categoryMybatisRepository;

    @Override
    public List<Category> selectAllCategory() {
        return categoryMybatisRepository.selectAllCategory();
    }

    @Override
    public Optional<Category> selectCategoryById(Long categoryId) {
        return categoryMybatisRepository.selectCategoryById(categoryId);
    }

    @Override
    public int save(Category category) {
        return categoryMybatisRepository.save(category);
    }

    @Override
    public int updateCategory(Category category) {
        return categoryMybatisRepository.updateCategory(category);
    }
}
