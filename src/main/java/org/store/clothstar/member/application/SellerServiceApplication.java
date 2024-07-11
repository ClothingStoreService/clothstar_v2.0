package org.store.clothstar.member.application;

import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.service.SellerService;

@Service
public class SellerServiceApplication {
    private final SellerService sellerService;

    public SellerServiceApplication(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    public Seller getSellerById(Long memberId) {
        return sellerService.getSellerById(memberId);
    }

    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        return sellerService.sellerSave(memberId, createSellerRequest);
    }
}