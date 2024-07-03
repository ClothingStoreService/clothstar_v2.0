package org.store.clothstar.member.application;

import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.service.AddressService;

import java.util.List;

@Service
public class AddressServiceApplication {
    private final AddressService addressService;

    public AddressServiceApplication(AddressService addressService) {
        this.addressService = addressService;
    }

    public List<AddressResponse> getMemberAllAddress(Long memberId) {
        return addressService.findMemberAllAddress(memberId);
    }

    public Long addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        return addressService.addrSave(memberId, createAddressRequest);
    }
}