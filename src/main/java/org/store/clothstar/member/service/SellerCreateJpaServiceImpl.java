package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.member.repository.MemberJpaRepository;
import org.store.clothstar.member.repository.SellerJpaRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellerCreateJpaServiceImpl implements SellerCreateService {
    private final SellerJpaRepository sellerJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private MemberEntity memberEntity;

    @Override
    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        validCheck(memberId, createSellerRequest);

        SellerEntity sellerEntity = new SellerEntity(createSellerRequest, memberEntity);
        sellerEntity = sellerJpaRepository.save(sellerEntity);

        return sellerEntity.getMemberId();
    }

    private void validCheck(Long memberId, CreateSellerRequest createSellerRequest) {
        memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        sellerJpaRepository.findById(memberId).ifPresent(m -> {
            throw new IllegalArgumentException("이미 판매자 가입이 되어 있습니다.");
        });

        sellerJpaRepository.findByBizNo(createSellerRequest.getBizNo()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 사업자 번호 입니다.");
        });

        sellerJpaRepository.findByBrandName(createSellerRequest.getBrandName()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 브랜드 이름 입니다.");
        });
    }
}
