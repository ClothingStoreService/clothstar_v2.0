package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.member.domain.MemberRole;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberJpaRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceJpaUnitTest {
    @Autowired
    private MemberBasicServiceImpl memberBasicServiceImpl;

    @Autowired
    private MemberSignupJpaServiceImpl memberSignupJpaServiceImpl;

    @Autowired
    private MemberPasswordUpdateServiceImpl memberPasswordUpdateServiceImpl;

    @Autowired
    private MemberDeleteServiceImpl memberDeleteServiceImpl;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long memberId;
    private MemberEntity memberEntity;

    @DisplayName("회원가입후 memberId를 반환한다.")
    @BeforeEach
    public void getMemberId_getAccessToken() {
        //given && when
        memberId = memberSignupJpaServiceImpl.signUp(getCreateMemberRequest());
        memberEntity = memberJpaRepository.findById(memberId).get();

        //then
        assertThat(memberId).isNotNull();
    }

    @DisplayName("회원 권한에 대한 수정을 확인 한다. 다른 필드는 변경되면 안된다")
    @Test
    void modifyMemberAuthUnitTest() {
        //회원가입시 기본 User 권한으로 적용 되었는지 확인
        assertThat(memberEntity.getRole()).isEqualTo(MemberRole.USER);
        String name = memberEntity.getName();

        //given
        ModifyMemberRequest modifyMemberRequest = ModifyMemberRequest.builder()
                .role(MemberRole.SELLER)
                .build();

        //when
        memberBasicServiceImpl.modifyMember(memberId, modifyMemberRequest);

        //then
        //Seller 권한으로 변경 되었는지 확인
        MemberEntity modifiedMember = memberJpaRepository.findById(memberId).get();
        assertThat(modifiedMember.getRole()).isEqualTo(MemberRole.SELLER);
        assertThat(name).isEqualTo(modifiedMember.getName()); //이름은 변경 안됐는지 확인한다.
    }

    @DisplayName("회원 이름에 대한 수정을 확인 한다. 다른 필드는 변경되면 안된다.")
    @Test
    void modifyMemberNameUnitTest() {
        //given
        MemberRole memberRole = memberEntity.getRole();
        ModifyMemberRequest modifyMemberRequest = ModifyMemberRequest.builder()
                .name("아이언맨")
                .build();

        //when
        memberBasicServiceImpl.modifyMember(memberId, modifyMemberRequest);

        //then
        //Seller 권한으로 변경 되었는지 확인
        MemberEntity modifiedMember = memberJpaRepository.findById(memberId).get();
        assertThat(memberRole).isEqualTo(modifiedMember.getRole()); //권한은 변경 안됐는지 확인 확인한다.
    }

    @DisplayName("비밀번호가 변경 됐는지 확인한다.")
    @Test
    void modifyPasswordUnitTest() {
        //given
        String originPassword = memberEntity.getPassword();
        String modifyPassword = "zxcvasdf123";

        //when
        memberPasswordUpdateServiceImpl.updatePassword(memberId, modifyPassword);

        //then
        MemberEntity modifiedMember = memberJpaRepository.findById(memberId).get();
        assertThat(modifiedMember.getPassword()).isNotEqualTo(originPassword);
    }

    @DisplayName("회원삭제시 deleteAt 필드가 현재시간으로 업데이트 됐는지 확인한다.")
    @Test
    void memberDeleteAtUnitTest() {
        //given
        LocalDateTime originDeletedAt = memberEntity.getDeletedAt();

        //when
        memberDeleteServiceImpl.updateDeleteAt(memberId);

        //then
        assertThat(originDeletedAt).isNull();
        MemberEntity modifiedMember = memberJpaRepository.findById(memberId).get();
        assertThat(modifiedMember.getDeletedAt()).isNotNull();
    }

    @DisplayName("같은 아이디로 회원가입시 에러 메시지 응답한다.")
    @Test
    void signUpValid_idDuplicateCheck() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            memberSignupJpaServiceImpl.signUp(getCreateMemberRequest());
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 아이디 입니다.");
    }

    private CreateMemberRequest getCreateMemberRequest() {
        String email = "test3@test.com";
        String password = "testl122";
        String name = "현수";
        String telNo = "010-1234-1245";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo
        );

        return createMemberRequest;
    }
}