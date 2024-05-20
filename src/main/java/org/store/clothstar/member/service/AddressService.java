package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.repository.AddressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressInfoRepository;

    public List<AddressResponse> getMemberAllAddress(Long memberId) {
        List<Address> memberAddressList = addressInfoRepository.findMemberAllAddress(memberId);

        List<AddressResponse> memberAddressResponseList = memberAddressList.stream()
                .map(AddressResponse::new)
                .collect(Collectors.toList());

        return memberAddressResponseList;
    }

    public MessageDTO addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        Address address = createAddressRequest.toAddress(memberId);

        int result = addressInfoRepository.save(address);
        if (result == 0) {
            throw new IllegalArgumentException("회원 배송지 주소가 저장되지 않았습니다.");
        }

        return MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "addressId : " + address.getAddressId() + " 회원 배송지 주소가 정상적으로 저장 되었습니다."
        );
    }
}