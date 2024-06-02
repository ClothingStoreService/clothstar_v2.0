package org.store.clothstar.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.application.MemberServiceApplication;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.repository.MemberMybatisRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceApplicationMockUnitTest {
    @Mock
    MemberMybatisRepository memberMybatisRepository;

    @InjectMocks
    MemberServiceApplication memberServiceApplication;


    @DisplayName("회원아이디로 회원 조회 테스트(마이바티스 사용)")
    @Test
    void getMemberTest() {
        //given
        Member member = mock(Member.class);
        given(memberMybatisRepository.findById(anyLong())).willReturn(Optional.of(member));
        when(member.getMemberId()).thenReturn(1L);

        //when
        MemberResponse memberResponse = memberServiceApplication.getMemberById(1L);

        //then
        verify(memberMybatisRepository, times(1))
                .findById(anyLong());

        assertThat(memberResponse.getMemberId()).isEqualTo(member.getMemberId());
    }

    @DisplayName("이메일이 중복된 경우의 단위 테스트(마이바티스 사용)")
    @Test
    void duplicateEmailCheckTest() {
        //given
        String email = "test@test.com";
        Member member = mock(Member.class);
        given(memberMybatisRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        //when
        //MessageDTO message = memberServiceApplication.emailCheck(email);

        //then
        //assertThat(message.getMessage()).isEqualTo("이미 사용중인 이메일 입니다.");
    }

    @DisplayName("이메일이 중복되지 않은 경우의 단위 테스트(마이바티스 사용)")
    @Test
    void duplicateEmailCheckTest2() {
        //given
        String email = "test@test.com";
        given(memberMybatisRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        //MessageDTO message = memberServiceApplication.emailCheck(email);

        //then
        //assertThat(message.getMessage()).isEqualTo("사용 가능한 이메일 입니다.");
    }
}