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

    @Override
    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by member: " + memberId));

        SellerEntity sellerEntity = new SellerEntity(createSellerRequest, memberEntity);
        sellerEntity = sellerJpaRepository.save(sellerEntity);

        return sellerEntity.getMemberId();
    }
}
