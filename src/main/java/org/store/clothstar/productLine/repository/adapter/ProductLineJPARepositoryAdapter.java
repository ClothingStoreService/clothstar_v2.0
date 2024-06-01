package org.store.clothstar.productLine.repository.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;
import org.store.clothstar.productLine.repository.ProductLineRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductLineJPARepositoryAdapter implements ProductLineRepository {

    private final ProductLineJPARepository productLineJPARepository;


    @Override
    public List<ProductLine> selectAllProductLinesNotDeleted() {
        return null;
    }

    @Override
    public Optional<ProductLine> selectByProductLineId(Long productId) {
        return Optional.empty();
    }

    @Override
    public Optional<ProductLineWithProductsResponse> findProductLineWithOptionsById(Long productId) {
        return Optional.empty();
    }
}
