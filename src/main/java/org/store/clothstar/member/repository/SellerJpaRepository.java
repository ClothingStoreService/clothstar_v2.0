package org.store.clothstar.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.member.entity.SellerEntity;

import java.util.Optional;

public interface SellerJpaRepository extends JpaRepository<SellerEntity, Long> {
    Optional<SellerEntity> findByBizNo(String bizNo);

    Optional<SellerEntity> findByBrandName(String brandName);
}
