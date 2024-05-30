package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.SellerMybatisRepository;

@Service
@Slf4j
public class SellerBasicServiceImpl implements SellerBasicService {
    private final SellerMybatisRepository sellerMybatisRepository;

    public SellerBasicServiceImpl(SellerMybatisRepository sellerMybatisRepository) {
        this.sellerMybatisRepository = sellerMybatisRepository;
    }

    @Override
    public Seller getSellerById(Long memberId) {
        return sellerMybatisRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));
    }

    @Override
    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        Seller seller = createSellerRequest.toSeller(memberId);

        int result = sellerMybatisRepository.save(seller);
        if (result == 0) {
            throw new IllegalArgumentException("판매자 가입이 되지 않았습니다.");
        }

        return seller.getMemberId();
    }
}
