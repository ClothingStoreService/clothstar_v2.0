package org.store.clothstar.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.member.domain.Member;

@Getter
@Builder
public class MemberSimpleResponse {
    private Long memberId;
    private String email;
    private String name;
    private String telNo;

    public static MemberSimpleResponse from(Member member) {
        return MemberSimpleResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .name(member.getName())
                .telNo(member.getTelNo())
                .build();
    }
}
