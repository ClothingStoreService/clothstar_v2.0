package org.store.clothstar.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.service.SellerBasicService;

@Service
@RequiredArgsConstructor
public class SellerServiceApplication {
    private final SellerBasicService sellerBasicService;

    public Seller getSellerById(Long memberId) {
        return sellerBasicService.getSellerById(memberId);
    }

    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        return sellerBasicService.sellerSave(memberId, createSellerRequest);
    }
}