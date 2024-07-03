package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.entity.SellerEntity;

public interface SellerService {
    SellerEntity getSellerById(Long memberId);

    Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest);
}
