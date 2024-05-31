package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.repository.AddressMybatisRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressCreateMybatisServiceImpl implements AddressCreateService {
    private final AddressMybatisRepository addressMybatisRepository;

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
