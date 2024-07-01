package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.repository.SellerRepository;

@Service
@Slf4j
public class SellerBasicServiceImpl implements SellerBasicService {
    private final SellerRepository sellerRepository;

    public SellerBasicServiceImpl(
            @Qualifier("sellerJpaRepositoryAdapter") SellerRepository sellerRepository) {
        //@Qualifier("sellerMybatisRepository") SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Seller getSellerById(Long memberId) {
        return sellerRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));
    }
}
