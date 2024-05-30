package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.repository.SellerMybatisRepository;
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

    @Override
    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        Seller seller = createSellerRequest.toSeller(memberId);

        int result = sellerRepository.save(seller);
        if (result == 0) {
            throw new IllegalArgumentException("판매자 가입이 되지 않았습니다.");
        }

        return seller.getMemberId();
    }
}
