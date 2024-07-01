package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.repository.AddressRepository;

import java.util.List;

@Service
@Slf4j
public class AddressBasicServiceImpl implements AddressBasicService {
    private final AddressRepository addressRepository;

    public AddressBasicServiceImpl(
            @Qualifier("addressJpaRepositoryAdapter") AddressRepository addressRepository) {
        //@Qualifier("addressMybatisRepository") AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressResponse> findMemberAllAddress(Long memberId) {
        return addressRepository.findMemberAllAddress(memberId).stream()
                .map(AddressResponse::new)
                .toList();
    }
}
