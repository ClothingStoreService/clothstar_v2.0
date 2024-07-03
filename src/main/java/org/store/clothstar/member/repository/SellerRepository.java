package org.store.clothstar.member.repository;

import org.store.clothstar.member.entity.SellerEntity;

import java.util.Optional;

public interface SellerRepository {
    Optional<SellerEntity> findByBizNo(String bizNo);

    Optional<SellerEntity> findByBrandName(String brandName);

    Optional<SellerEntity> findById(Long memberId);

    SellerEntity save(SellerEntity sellerEntity);
}
