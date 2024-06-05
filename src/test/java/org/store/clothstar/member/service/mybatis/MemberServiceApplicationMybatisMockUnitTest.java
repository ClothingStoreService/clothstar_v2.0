package org.store.clothstar.member.service.mybatis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.repository.MemberMybatisRepository;
import org.store.clothstar.member.service.MemberBasicServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceApplicationMybatisMockUnitTest {
    @Mock
    MemberMybatisRepository memberMybatisRepository;

    @InjectMocks
    MemberBasicServiceImpl memberBasicService;
    private String email = "test@test.com";

    @DisplayName("회원아이디로 회원울 조회한 테스트 이다.(Mybatis)")
    @Test
    void getMemberTest() {
        //given
        Member member = mock(Member.class);
        given(memberMybatisRepository.findById(anyLong())).willReturn(Optional.of(member));
        when(member.getMemberId()).thenReturn(1L);

        //when
        MemberResponse memberResponse = memberBasicService.getMemberById(1L);

        //then
        verify(memberMybatisRepository, times(1))
                .findById(anyLong());

        assertThat(memberResponse.getMemberId()).isEqualTo(member.getMemberId());
    }

    @DisplayName("email로 가입된 회원이 있는지 확인한다. 았으면 true를 반환 (Mybatis)")
    @Test
    void duplicateEmailCheckTest() {
        //given
        Member member = mock(Member.class);
        given(memberMybatisRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        //when
        boolean isMemberEmail = memberBasicService.getMemberByEmail(email);

        //then
        assertThat(isMemberEmail).isEqualTo(true);
    }

    @DisplayName("email로 가입된 회원이 있는지 확인한다. 없으면 false를 반환 (Mybatis)")
    @Test
    void duplicateEmailCheckTest2() {
        //given
        given(memberMybatisRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        boolean isMemberEmail = memberBasicService.getMemberByEmail(email);

        //then
        assertThat(isMemberEmail).isEqualTo(false);
    }
}