package org.store.clothstar.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.application.AddressServiceApplication;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.repository.AddressMybatisRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AddressBasicServiceApplicationMockUnitTest {
    @Mock
    AddressMybatisRepository addressMybatisRepository;

    @InjectMocks
    AddressServiceApplication addressServiceApplication;

    private Long memberId = 1L;

    @DisplayName("회원 배송지 조회 단위 테스트")
    @Test
    void getMemberAddrUnitTest() {
        //given
        given(addressMybatisRepository.findMemberAllAddress(any())).willReturn(getAddressList());

        //when
        List<AddressResponse> memberAddressResponseList = addressServiceApplication.getMemberAllAddress(memberId);

        //then
        verify(addressMybatisRepository, times(1)).findMemberAllAddress((anyLong()));
        assertThat(memberAddressResponseList.size()).isEqualTo(2);
    }

    private List<Address> getAddressList() {
        Address address = mock(Address.class);
        return List.of(address, address);
    }
}