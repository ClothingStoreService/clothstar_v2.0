package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<AddressResponse> findMemberAllAddress(Long memberId) {
        return addressRepository.findAddressListByMemberId(memberId).stream()
                .map(AddressResponse::new)
                .toList();
    }

    @Override
    public Long addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        AddressEntity addressEntity = createAddressRequest.toAddressEntity(memberEntity);
        addressEntity = addressRepository.save(addressEntity);

        return addressEntity.getAddressId();
    }
}
