package org.store.clothstar.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberPasswordUpdateServiceUnitTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberServiceImpl;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("이전 암호와 같은 비밀번호로 수정시 에러 메시지를 반환한다.")
    @Test
    void duplicatePassword() {
        //given
        Long memberId = 1L;
        String password = "test1234";
        Member member = mock(Member.class);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        when(member.getPassword()).thenReturn(password);

        //when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            memberServiceImpl.updatePassword(memberId, password);
        });

        //then
        verify(memberRepository, times(1)).findById(memberId);
        assertThat(exception.getMessage()).isEqualTo("이전 비밀번호와 같은 비밀번호 입니다.");
    }
}