package org.store.clothstar.member.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.service.AddressBasicService;
import org.store.clothstar.member.service.AddressCreateService;

import java.util.List;

@Service
public class AddressServiceApplication {
    private final AddressBasicService addressBasicService;
    private final AddressCreateService addressCreateService;

    public AddressServiceApplication(
            AddressBasicService addressBasicService,
            @Qualifier("addressCreateJpaServiceImpl") AddressCreateService addressCreateService) {
        //@Qualifier("addressCreateMybatisServiceImpl") AddressCreateService addressCreateService) {
        this.addressBasicService = addressBasicService;
        this.addressCreateService = addressCreateService;
    }

    public List<AddressResponse> getMemberAllAddress(Long memberId) {
        return addressBasicService.findMemberAllAddress(memberId);
    }

    public Long addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        return addressCreateService.addrSave(memberId, createAddressRequest);
    }
}