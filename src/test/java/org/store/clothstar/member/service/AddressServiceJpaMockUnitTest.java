package org.store.clothstar.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.AddressRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AddressServiceJpaMockUnitTest {
    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    AddressServiceImpl AddressServiceImpl;

    private Long memberId = 1L;

    @DisplayName("회원 배송지 조회 단위 테스트")
    @Test
    void getMemberAddrJpaUnitTest() {
        //given
        AddressEntity address = mock(AddressEntity.class);
        MemberEntity member = mock(MemberEntity.class);
        given(address.getMember()).willReturn(member);
        List<AddressEntity> addresses = List.of(address, address);
        given(addressRepository.findAddressListByMemberId(any())).willReturn(addresses);

        //when
        List<AddressResponse> memberAddressResponseList = AddressServiceImpl.findMemberAllAddress(memberId);

        //then
        verify(addressRepository, times(1)).findAddressListByMemberId((anyLong()));
        assertThat(memberAddressResponseList.size()).isEqualTo(2);
    }
}