package org.store.clothstar.member.util;

import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateMemberRequest;

public class CreateObject {
    public static CreateMemberRequest getCreateMemberRequest() {
        String email = "test3@test.com";
        String password = "testl122";
        String name = "현수";
        String telNo = "010-1234-1245";
        String certifyNum = "asdf123";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo, certifyNum
        );

        return createMemberRequest;
    }

    public static CreateMemberRequest getCreateMemberRequest(String email) {
        String password = "testl122";
        String name = "현수";
        String telNo = "010-1234-1245";
        String certifyNum = "asdf123";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo, certifyNum
        );

        return createMemberRequest;
    }

    public static CreateMemberRequest getCreateMemberRequest(String email, String certifyNum) {
        String password = "testl122";
        String name = "현수";
        String telNo = "010-1234-1245";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo, certifyNum
        );

        return createMemberRequest;
    }

    public static Member getMemberByCreateMemberRequestDTO(int i) {
        return Member.builder()
                .email("test" + i + "@test.com") // 유니크한 이메일 주소 생성
                .name("Test User" + i)
                .password("password")
                .telNo("010-1234-567" + i)
                .build();
    }

    public static Member getMemberByCreateMemberRequestDTO() {
        return getCreateMemberRequest().toMember();
    }
}
