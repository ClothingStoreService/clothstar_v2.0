package org.store.clothstar.member.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.member.domain.vo.MemberShoppingActivity;
import org.store.clothstar.member.dto.request.ModifyNameRequest;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity(name = "member")
public class Member extends BaseEntity {
    @Id
    private Long memberId;

    private String telNo;
    private String name;

    @Embedded
    MemberShoppingActivity memberShoppingActivity;

    public void updateName(ModifyNameRequest modifyNameRequest) {
        this.name = modifyNameRequest.getName();
    }
}
