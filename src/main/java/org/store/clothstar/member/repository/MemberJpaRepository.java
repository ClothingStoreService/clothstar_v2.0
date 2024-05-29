package org.store.clothstar.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
}