package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.domain.Seller;

public interface SellerService {
    Seller getSellerById(Long memberId);

    Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest);
}
