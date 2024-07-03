package org.store.clothstar.member.repository;

import org.store.clothstar.member.entity.AddressEntity;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {
    List<AddressEntity> findAddressListByMemberId(Long memberId);

    Optional<AddressEntity> findById(Long addressId);

    AddressEntity save(AddressEntity addressEntity);
}
