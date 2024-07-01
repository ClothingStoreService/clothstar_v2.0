package org.store.clothstar.member.repository;

import org.store.clothstar.member.domain.Seller;

import java.util.Optional;

public interface SellerRepository {
    Optional<Seller> findById(Long memberId);
}
