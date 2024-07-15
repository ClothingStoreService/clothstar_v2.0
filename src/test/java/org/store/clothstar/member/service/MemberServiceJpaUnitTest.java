package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.member.domain.Account;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.ModifyNameRequest;
import org.store.clothstar.member.repository.AccountRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.util.CreateObject;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceJpaUnitTest {
    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Long memberId;
    private Member member;
    private Account account;

    @DisplayName("회원가입후 memberId를 반환한다.")
    @BeforeEach
    public void getMemberId_getAccessToken() {
        //given && when
        account = accountRepository.save(CreateObject.getAccount());
        member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO(account.getAccountId()));
        memberId = member.getMemberId();

        //then
        assertThat(memberId).isNotNull();
    }

    @DisplayName("회원 이름에 대한 수정을 확인 한다.")
    @Test
    void modifyMemberNameUnitTest() {
        //given
        ModifyNameRequest modifyNameRequest = ModifyNameRequest.builder()
                .name("아이언맨")
                .build();

        //when
        memberServiceImpl.modifyName(memberId, modifyNameRequest);

        //then
        Member modifiedMember = memberRepository.findById(memberId).get();
    }

    @DisplayName("비밀번호가 변경 됐는지 확인한다.")
    @Test
    void modifyPasswordUnitTest() {
        //given
        String originPassword = account.getPassword();
        String modifyPassword = "zxcvasdf123";

        //when
        memberServiceImpl.updatePassword(memberId, modifyPassword);

        //then
        Account accountRepository = this.accountRepository.findById(memberId).get();
        assertThat(accountRepository.getPassword()).isNotEqualTo(originPassword);
    }

    @DisplayName("회원삭제시 deleteAt 필드가 현재시간으로 업데이트 됐는지 확인한다.")
    @Test
    void memberDeleteAtUnitTest() {
        //given
        LocalDateTime originDeletedAt = member.getDeletedAt();

        //when
        memberServiceImpl.updateDeleteAt(memberId);

        //then
        assertThat(originDeletedAt).isNull();
        Member modifiedMember = memberRepository.findById(memberId).get();
        assertThat(modifiedMember.getDeletedAt()).isNotNull();
    }

    @DisplayName("같은 아이디로 회원가입시 에러 메시지 응답한다.")
    @Test
    void signUpValid_idDuplicateCheck() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            memberServiceImpl.signUp(CreateObject.getCreateMemberRequest());
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 아이디 입니다.");
    }


}