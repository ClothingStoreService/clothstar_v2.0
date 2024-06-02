package org.store.clothstar.member.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberGrade;
import org.store.clothstar.member.domain.MemberRole;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity(name = "member")
public class MemberEntity extends BaseEntity {
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
    }

    public void updateMember(Member member, MemberEntity memberEntity) {
        this.name = (member.getName() == null || member.getName() == "") ? memberEntity.getName() : member.getName();
        this.role = (member.getRole() == null) ? memberEntity.getRole() : member.getRole();
        log.info("name : {}, role : {}", name, role);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}