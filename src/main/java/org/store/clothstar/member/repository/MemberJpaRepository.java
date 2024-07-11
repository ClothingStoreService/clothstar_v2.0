package org.store.clothstar.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.store.clothstar.member.domain.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepository {
    Optional<Member> findByEmail(String email);

    @Query(value = "select m from member m where m.deletedAt is null order by m.createdAt desc",
            countQuery = "select count(m) from member m")
    Page<Member> findAllOffsetPaging(Pageable pageable);

    @Query(value = "select m from member m where m.deletedAt is null order by m.createdAt desc",
            countQuery = "select count(m) from member m")
    Slice<Member> findAllSlicePaging(Pageable pageable);
}