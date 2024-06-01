package org.store.clothstar.category.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.category.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class JpaCategoryRepositoryAdapter implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Category> selectAllCategory() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public Optional<Category> selectCategoryById(Long categoryId) {
        return categoryJpaRepository.findById(categoryId);
    }

    @Override
    public int save(Category category) {
        categoryJpaRepository.save(category);
        return 1;
    }

    @Override
    public int updateCategory(Category category) {
        return 0;
    }
}
