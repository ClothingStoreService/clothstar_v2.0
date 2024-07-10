package org.store.clothstar.member.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.member.domain.vo.MemberShoppingActivity;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(unique = true)
    private String email;
    private String password;

    private String name;
    private String telNo;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Embedded
    MemberShoppingActivity memberShoppingActivity;

    public void updateMember(ModifyMemberRequest modifyMemberRequest, Member member) {
        this.name = (modifyMemberRequest.getName() == null || modifyMemberRequest.getName() == "")
                ? member.getName() : modifyMemberRequest.getName();
        this.role = (modifyMemberRequest.getRole() == null) ? member.getRole() : modifyMemberRequest.getRole();
        log.info("name : {}, role : {}", name, role);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}