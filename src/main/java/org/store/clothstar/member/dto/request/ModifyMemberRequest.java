package org.store.clothstar.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberRole;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModifyMemberRequest {
    private MemberRole role;

    public Member toMember(Long memberId) {
        return Member.builder()
                .memberId(memberId)
                .role(getRole())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}