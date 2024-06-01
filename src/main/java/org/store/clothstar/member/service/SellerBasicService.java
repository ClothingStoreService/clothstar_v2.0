package org.store.clothstar.member.service;

import org.store.clothstar.member.domain.Seller;

public interface SellerBasicService {
    Seller getSellerById(Long memberId);
}
