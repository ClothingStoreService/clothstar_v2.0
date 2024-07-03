package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.member.repository.MemberJpaRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.repository.SellerRepository;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final MemberRepository memberRepository;
    private MemberEntity memberEntity;

    public SellerServiceImpl(
            @Qualifier("sellerJpaRepository") SellerRepository sellerRepository,
            MemberJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public SellerEntity getSellerById(Long memberId) {
        return sellerRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));
    }

    @Override
    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        validCheck(memberId, createSellerRequest);

        SellerEntity sellerEntity = new SellerEntity(createSellerRequest, memberEntity);
        sellerEntity = sellerRepository.save(sellerEntity);

        return sellerEntity.getMemberId();
    }

    private void validCheck(Long memberId, CreateSellerRequest createSellerRequest) {
        memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        sellerRepository.findById(memberId).ifPresent(m -> {
            throw new IllegalArgumentException("이미 판매자 가입이 되어 있습니다.");
        });

        sellerRepository.findByBizNo(createSellerRequest.getBizNo()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 사업자 번호 입니다.");
        });

        sellerRepository.findByBrandName(createSellerRequest.getBrandName()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 브랜드 이름 입니다.");
        });
    }
}
