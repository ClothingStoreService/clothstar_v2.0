package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;

import java.util.List;

public interface AddressBasicService {
    List<AddressResponse> findMemberAllAddress(Long memberId);

    Long addrSave(Long memberId, CreateAddressRequest createAddressRequest);
}
