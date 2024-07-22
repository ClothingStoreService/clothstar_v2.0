package org.store.clothstar.member.dto.request;

import lombok.*;
import org.store.clothstar.member.domain.MemberRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ModifyMemberRequest {
    private String name;
    private MemberRole role;
}