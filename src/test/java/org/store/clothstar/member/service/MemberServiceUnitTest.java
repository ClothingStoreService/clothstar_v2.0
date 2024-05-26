package org.store.clothstar.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.member.domain.MemberRole;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
public class MemberServiceUnitTest {

    @Autowired
    MemberService memberService;

    @DisplayName("회원 권한 수정 단위 테스트")
    @Test
    void modifyMemberAuthUnitTest() {
        //given
        Long memberId = 54L;
        ModifyMemberRequest modifyMemberRequest = ModifyMemberRequest.builder()
                .name("토레스")
                .role(MemberRole.SELLER)
                .build();

        //when
        MessageDTO messageDTO = memberService.modifyMember(memberId, modifyMemberRequest);

        //then
        assertThat(messageDTO.isSuccess()).isTrue();
    }
}