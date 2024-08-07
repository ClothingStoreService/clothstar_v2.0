package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberRole;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
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

    private Long memberId;
    private Member member;

    @DisplayName("회원가입후 memberId를 반환한다.")
    @BeforeEach
    public void getMemberId_getAccessToken() {
        //given && when
        member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO());
        memberId = member.getMemberId();

        //then
        assertThat(memberId).isNotNull();
    }

    @DisplayName("회원 권한에 대한 수정을 확인 한다. 다른 필드는 변경되면 안된다")
    @Test
    void modifyMemberAuthUnitTest() {
        //회원가입시 기본 User 권한으로 적용 되었는지 확인
        assertThat(member.getRole()).isEqualTo(MemberRole.USER);
        String name = member.getName();

        //given
        ModifyMemberRequest modifyMemberRequest = ModifyMemberRequest.builder()
                .role(MemberRole.SELLER)
                .build();

        //when
        memberServiceImpl.modifyMember(memberId, modifyMemberRequest);

        //then
        //Seller 권한으로 변경 되었는지 확인
        Member modifiedMember = memberRepository.findById(memberId).get();
        assertThat(modifiedMember.getRole()).isEqualTo(MemberRole.SELLER);
        assertThat(name).isEqualTo(modifiedMember.getName()); //이름은 변경 안됐는지 확인한다.
    }

    @DisplayName("회원 이름에 대한 수정을 확인 한다. 다른 필드는 변경되면 안된다.")
    @Test
    void modifyMemberNameUnitTest() {
        //given
        MemberRole memberRole = member.getRole();
        ModifyMemberRequest modifyMemberRequest = ModifyMemberRequest.builder()
                .name("아이언맨")
                .build();

        //when
        memberServiceImpl.modifyMember(memberId, modifyMemberRequest);

        //then
        //Seller 권한으로 변경 되었는지 확인
        Member modifiedMember = memberRepository.findById(memberId).get();
        assertThat(memberRole).isEqualTo(modifiedMember.getRole()); //권한은 변경 안됐는지 확인 확인한다.
    }

    @DisplayName("비밀번호가 변경 됐는지 확인한다.")
    @Test
    void modifyPasswordUnitTest() {
        //given
        String originPassword = member.getPassword();
        String modifyPassword = "zxcvasdf123";

        //when
        memberServiceImpl.updatePassword(memberId, modifyPassword);

        //then
        Member modifiedMember = memberRepository.findById(memberId).get();
        assertThat(modifiedMember.getPassword()).isNotEqualTo(originPassword);
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