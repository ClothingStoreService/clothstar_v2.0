package org.store.clothstar.member.repository;

import org.store.clothstar.member.domain.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {
    List<Address> findAddressListByMemberId(Long memberId);

    Optional<Address> findById(Long addressId);

    Address save(Address address);
}
