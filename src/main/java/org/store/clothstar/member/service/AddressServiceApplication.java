package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceApplication {
    private final AddressBasicService addressBasicService;

    public List<AddressResponse> getMemberAllAddress(Long memberId) {
        return addressBasicService.findMemberAllAddress(memberId);
    }

    public Long addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        return addressBasicService.addrSave(memberId, createAddressRequest);
    }
}