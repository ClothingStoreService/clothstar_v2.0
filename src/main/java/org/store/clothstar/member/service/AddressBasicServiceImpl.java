package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.repository.AddressMybatisRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddressBasicServiceImpl implements AddressBasicService {
    private final AddressMybatisRepository addressMybatisRepository;

    public AddressBasicServiceImpl(@Qualifier("addressMybatisRepository") AddressMybatisRepository addressMybatisRepository) {
        this.addressMybatisRepository = addressMybatisRepository;
    }

    @Override
    public List<AddressResponse> findMemberAllAddress(Long memberId) {
        return addressMybatisRepository.findMemberAllAddress(memberId).stream()
                .map(AddressResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Long addrSave(Long memberId, CreateAddressRequest createAddressRequest) {
        Address address = createAddressRequest.toAddress(memberId);

        int result = addressMybatisRepository.save(address);
        if (result == 0) {
            throw new IllegalArgumentException("회원 배송지 주소가 저장되지 않았습니다.");
        }
        
        return address.getAddressId();
    }
}
