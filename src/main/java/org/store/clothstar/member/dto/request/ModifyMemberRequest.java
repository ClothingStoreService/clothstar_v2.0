package org.store.clothstar.member.dto.request;

import lombok.*;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberRole;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ModifyMemberRequest {
    private String name;
    private MemberRole role;

    public Member toMember(Long memberId) {
        return Member.builder()
                .memberId(memberId)
                .name(getName())
                .role(getRole())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}