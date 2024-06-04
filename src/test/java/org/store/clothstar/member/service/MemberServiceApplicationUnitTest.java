package org.store.clothstar.member.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceApplicationUnitTest {
    @Autowired
    private MemberBasicServiceImpl memberBasicServiceImpl;

    @Autowired
    private MemberSignupJpaServiceImpl memberSignupJpaServiceImpl;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long memberId;
    private MemberEntity memberEntity;

    @DisplayName("회원가입한 멤버아이디와, 인증에 필요한 access 토큰을 가져옵니다.")
    @BeforeEach
    public void getMemberId_getAccessToken() {
        memberId = memberSignupJpaServiceImpl.signUp(getCreateMemberRequest());
        memberEntity = memberJpaRepository.findById(memberId).get();
    }

    @DisplayName("회원 권한에 대한 수정을 확인 한다.")
    @Test
    void modifyMemberAuthUnitTest() {
        //회원가입시 기본 User 권한으로 적용 되었는지 확인
        assertThat(memberEntity.getRole()).isEqualTo(MemberRole.USER);
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
    }

    @DisplayName("같은 아이디로 회원가입시 에러 메시지 응답한다.")
    @Test
    void signUpValid_idDuplicate() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            memberSignupJpaServiceImpl.signUp(getCreateMemberRequest());
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 아이디 입니다.");
    }

    private CreateMemberRequest getCreateMemberRequest() {
        String email = "test@test.com";
        String password = "testl122";
        String name = "현수";
        String telNo = "010-1234-1245";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo
        );

        return createMemberRequest;
    }
}