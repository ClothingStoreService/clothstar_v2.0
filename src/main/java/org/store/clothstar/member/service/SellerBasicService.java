package org.store.clothstar.member.service;

import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;

public interface SellerBasicService {
    Seller getSellerById(Long memberId);

    Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest);
}
