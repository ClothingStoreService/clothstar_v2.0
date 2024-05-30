package org.store.clothstar.member.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.entity.AddressEntity;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AddressJpaRepositoryAdapter implements AddressRepository {
    private final AddressJpaRepository addressJpaRepository;

    @Override
    public List<Address> findMemberAllAddress(Long memberId) {
        return addressJpaRepository.findAddressListByMemberId(memberId).stream()
                .map(Address::new)
                .toList();
    }

    @Override
    public Optional<Address> findById(Long addressId) {
        return addressJpaRepository.findById(addressId).map(Address::new);
    }

    @Override
    public int save(Address address) {
        addressJpaRepository.save(new AddressEntity(address));
        return 1;
    }
}
