package org.store.clothstar.member.domain;

import lombok.*;
import org.store.clothstar.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long memberId;
    private String email;
    private String password;
    private String name;
    private String telNo;
    private int totalPaymentPrice;
    private int point;
    private MemberRole role;
    private MemberGrade grade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Member(MemberEntity memberEntity) {
        this.memberId = memberEntity.getMemberId();
        this.email = memberEntity.getEmail();
        this.password = memberEntity.getPassword();
        this.name = memberEntity.getName();
        this.telNo = memberEntity.getTelNo();
        this.totalPaymentPrice = memberEntity.getTotalPaymentPrice();
        this.point = memberEntity.getPoint();
        this.role = memberEntity.getRole();
        this.grade = memberEntity.getGrade();
        this.createdAt = memberEntity.getCreatedAt();
        this.updatedAt = memberEntity.getUpdatedAt();
        this.deletedAt = memberEntity.getDeletedAt();
    }
}
