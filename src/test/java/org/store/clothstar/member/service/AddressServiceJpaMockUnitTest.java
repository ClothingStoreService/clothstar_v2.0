package org.store.clothstar.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.vo.AddressInfo;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.util.CreateObject;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AddressServiceJpaMockUnitTest {
    @Mock
    AddressRepository addressRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    AddressServiceImpl AddressServiceImpl;

    private Long memberId = 1L;

    @DisplayName("회원 배송지 조회 단위 테스트")
    @Test
    void getMemberAddrJpaUnitTest() {
        //given
        Address address = mock(Address.class);
        Member member = mock(Member.class);
        AddressInfo addressInfo = mock(AddressInfo.class);
        given(address.getMember()).willReturn(member);
        given(address.getAddressInfo()).willReturn(addressInfo);
        List<Address> addresses = List.of(address, address);
        given(addressRepository.findAddressListByMemberId(any())).willReturn(addresses);
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(CreateObject.getMemberByCreateMemberRequestDTO(anyLong())));

        //when
        List<AddressResponse> memberAddressResponseList = AddressServiceImpl.findMemberAllAddress(memberId);

        //then
        verify(addressRepository, times(1)).findAddressListByMemberId((anyLong()));
        assertThat(memberAddressResponseList.size()).isEqualTo(2);
    }
}