package org.store.clothstar.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.member.domain.Seller;

import java.util.Optional;

public interface SellerJpaRepository extends JpaRepository<Seller, Long>, SellerRepository {
    Optional<Seller> findByBizNo(String bizNo);

    Optional<Seller> findByBrandName(String brandName);
}
