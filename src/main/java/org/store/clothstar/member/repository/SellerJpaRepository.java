package org.store.clothstar.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.member.entity.SellerEntity;

public interface SellerJpaRepository extends JpaRepository<SellerEntity, Long> {
}
