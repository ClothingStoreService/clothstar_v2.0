package org.store.clothstar.member.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.member.dto.request.ModifyNameRequest;

import java.lang.reflect.Member;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile {
    String name;

    public void updateName(ModifyNameRequest modifyNameRequest, Member member) {
        this.name = name;
    }
}
