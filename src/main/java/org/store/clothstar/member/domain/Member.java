package org.store.clothstar.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
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
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
