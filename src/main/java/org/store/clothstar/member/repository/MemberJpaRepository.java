package org.store.clothstar.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long>, MemberRepository {
    Optional<MemberEntity> findByEmail(String email);

    @Query(value = "select m from member m",
            countQuery = "select count(m) from member m")
    Page<MemberEntity> findAllOffsetPaging(Pageable pageable);
}