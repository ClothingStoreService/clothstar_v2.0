package org.store.clothstar.member.dto.request;

import lombok.*;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberRole;
import org.store.clothstar.member.entity.MemberEntity;

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
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public MemberEntity toMemberEntity(Long memberId) {
        return MemberEntity.builder()
                .memberId(memberId)
                .name(getName())
                .role(getRole())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}