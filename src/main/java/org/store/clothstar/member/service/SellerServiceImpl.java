package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.DuplicatedBizNoException;
import org.store.clothstar.common.error.exception.DuplicatedBrandNameException;
import org.store.clothstar.common.error.exception.DuplicatedSellerException;
import org.store.clothstar.common.error.exception.NotFoundMemberException;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.MemberJpaRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.repository.SellerRepository;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final MemberRepository memberRepository;
    private Member member;

    public SellerServiceImpl(
            @Qualifier("sellerJpaRepository") SellerRepository sellerRepository,
            MemberJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Seller getSellerById(Long memberId) {
        return sellerRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        validCheck(memberId, createSellerRequest);

        Seller seller = new Seller(createSellerRequest, member);
        seller = sellerRepository.save(seller);

        return seller.getMemberId();
    }

    private void validCheck(Long memberId, CreateSellerRequest createSellerRequest) {
        member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        sellerRepository.findById(memberId).ifPresent(m -> {
            throw new DuplicatedSellerException(ErrorCode.DUPLICATED_SELLER);
        });

        sellerRepository.findByBizNo(createSellerRequest.getBizNo()).ifPresent(m -> {
            throw new DuplicatedBizNoException(ErrorCode.DUPLICATED_BIZNO);
        });

        sellerRepository.findByBrandName(createSellerRequest.getBrandName()).ifPresent(m -> {
            throw new DuplicatedBrandNameException(ErrorCode.DUPLICATED_BRAND_NAME);
        });
    }
}
