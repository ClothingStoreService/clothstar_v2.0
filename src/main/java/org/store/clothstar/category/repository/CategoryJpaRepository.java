package org.store.clothstar.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.entity.CategoryEntity;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

}
