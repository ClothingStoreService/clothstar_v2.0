package org.store.clothstar.member.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.entity.AddressEntity;

import java.util.List;

@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {

    @Query("SELECT addr FROM address addr WHERE addr.member.memberId = :memberId")
    List<AddressEntity> findAddressListByMemberId(@Param("memberId") Long memberId);
}
