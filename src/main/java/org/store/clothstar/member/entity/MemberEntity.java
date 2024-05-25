package org.store.clothstar.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberGrade;
import org.store.clothstar.member.domain.MemberRole;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String telNo;
    @ColumnDefault(value = "0")
    private int totalPaymentPrice;
    @ColumnDefault(value = "0")
    private int point;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    @Enumerated(EnumType.STRING)
    private MemberGrade grade;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    public MemberEntity(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.name = member.getName();
        this.telNo = member.getTelNo();
        this.totalPaymentPrice = member.getTotalPaymentPrice();
        this.point = member.getPoint();
        this.role = member.getRole();
        this.grade = member.getGrade();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
        this.deletedAt = member.getDeletedAt();
    }

    public void updateMember(Member member, MemberEntity memberEntity) {
        this.name = (member.getName() == null || member.getName() == "") ? memberEntity.getName() : member.getName();
        this.role = (member.getRole() == null) ? memberEntity.getRole() : member.getRole();
        this.modifiedAt = member.getModifiedAt();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}