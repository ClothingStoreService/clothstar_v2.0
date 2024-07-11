package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.NotFoundMemberException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
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
        memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        return addressRepository.findAddressListByMemberId(memberId).stream()
                .map(AddressResponse::new)
                .toList();
    }

    @Override
    public Long addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        Address address = createAddressRequest.toAddress(member);
        address = addressRepository.save(address);

        return address.getAddressId();
    }
}
