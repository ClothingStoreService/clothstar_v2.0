package org.store.clothstar.productLine.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.ProductOption;
import org.store.clothstar.productLine.repository.ProductLineRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductLineRepository productLineRepository;

    @Transactional
    public ProductOption createProductOption(Long productLineId, CreateProductOptionRequest request) {
        ProductLine productLine = productLineRepository.findById(productLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProductLine not found"));

    }
}
