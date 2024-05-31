package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.CreateSellerRequest;

public interface SellerCreateService {
    Long sellerSave(Long memberId, CreateSellerRequest createSellerRequest);
}
