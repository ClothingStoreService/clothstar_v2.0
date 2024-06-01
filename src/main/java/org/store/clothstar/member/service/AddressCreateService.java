package org.store.clothstar.member.service;

import org.store.clothstar.member.dto.request.CreateAddressRequest;

public interface AddressCreateService {
    Long addrSave(Long memberId, CreateAddressRequest createAddressRequest);
}
