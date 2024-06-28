package org.store.clothstar.common.config;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.product.repository.ProductJPARepositoryAdapter;
import org.store.clothstar.product.repository.ProductRepository;
import org.store.clothstar.product.repository.UpperProductRepository;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;
import org.store.clothstar.productLine.repository.ProductLineRepository;
import org.store.clothstar.productLine.repository.UpperProductLineRepository;
import org.store.clothstar.productLine.repository.adapter.ProductLineJPARepositoryAdapter;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {
    @Bean
    @ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa", matchIfMissing = true)
    public UpperProductRepository productRepository(ProductJPARepository productJPARepository, ProductLineJPARepository productLineJPARepository) {
        return new ProductJPARepositoryAdapter(productJPARepository, productLineJPARepository);
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository.type", havingValue = "mybatis")
    public UpperProductRepository productRepository(SqlSessionTemplate sqlSessionTemplate) {
        return sqlSessionTemplate.getMapper(ProductRepository.class);
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa", matchIfMissing = true)
    public UpperProductLineRepository productLineRepository(CategoryJpaRepository categoryJpaRepository,
                                                            SellerRepository sellerRepository,
                                                            ProductLineJPARepository productLineJPARepository,
                                                            ProductJPARepository productJPARepository) {
        return new ProductLineJPARepositoryAdapter(categoryJpaRepository, sellerRepository, productLineJPARepository, productJPARepository);
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository.type", havingValue = "mybatis")
    public UpperProductLineRepository productLineRepository(SqlSessionTemplate sqlSessionTemplate) {
        return sqlSessionTemplate.getMapper(ProductLineRepository.class);
}
