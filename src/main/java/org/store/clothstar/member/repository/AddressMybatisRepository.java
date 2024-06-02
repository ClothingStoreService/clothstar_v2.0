package org.store.clothstar.member.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.member.domain.Address;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AddressMybatisRepository extends AddressRepository {
    List<Address> findMemberAllAddress(Long memberId);

    int save(Address address);

    Optional<Address> findById(Long addressId);
}
