package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.AddressJpaRepository;
import org.store.clothstar.member.repository.MemberJpaRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressCreateJpaServiceImpl implements AddressCreateService {
    private final AddressJpaRepository addressJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Long addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        AddressEntity addressEntity = createAddressRequest.toAddressEntity(memberEntity);
        addressEntity = addressJpaRepository.save(addressEntity);

        return addressEntity.getAddressId();
    }
}
