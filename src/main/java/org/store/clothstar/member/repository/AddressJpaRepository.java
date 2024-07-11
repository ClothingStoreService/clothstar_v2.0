package org.store.clothstar.member.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressJpaRepository extends JpaRepository<Address, Long>, AddressRepository {
    @Query("SELECT addr FROM address addr WHERE addr.member.memberId = :memberId")
    List<Address> findAddressListByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT addr FROM address addr WHERE addr.member.memberId = :memberId AND addr.defaultAddress IS TRUE")
    Optional<Address> findByMemberIdAndDefaultAddress(Long memberId);
}