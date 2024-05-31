package org.store.clothstar.member.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.service.SellerBasicService;
import org.store.clothstar.member.service.SellerCreateService;

@Service
public class SellerServiceApplication {
    private final SellerBasicService sellerBasicService;
    private final SellerCreateService sellerCreateService;

    public SellerServiceApplication(
            SellerBasicService sellerBasicService,
            @Qualifier("sellerCreateJpaServiceImpl") SellerCreateService sellerCreateService) {
        //@Qualifier("sellerCreateMybatisServiceImpl") SellerCreateService sellerCreateService) {
        this.sellerBasicService = sellerBasicService;
        this.sellerCreateService = sellerCreateService;
    }

    public Seller getSellerById(Long memberId) {
        return sellerBasicService.getSellerById(memberId);
    }

    public Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest) {
        return sellerCreateService.sellerSave(memberId, createSellerRequest);
    }
}