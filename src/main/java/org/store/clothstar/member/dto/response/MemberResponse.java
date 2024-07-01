package org.store.clothstar.member.dto.response;

import lombok.Getter;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberGrade;
import org.store.clothstar.member.entity.MemberEntity;

@Getter
public class MemberResponse {
    private Long memberId;
    private String email;
    private String name;
    private String telNo;
    private int totalPaymentPrice;
    private MemberGrade grade;

    public MemberResponse(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.telNo = member.getTelNo();
        this.totalPaymentPrice = member.getTotalPaymentPrice();
        this.grade = member.getGrade();
    }

    public MemberResponse(MemberEntity memberEntity) {
        this.memberId = memberEntity.getMemberId();
        this.email = memberEntity.getEmail();
        this.name = memberEntity.getName();
        this.telNo = memberEntity.getTelNo();
        this.totalPaymentPrice = memberEntity.getTotalPaymentPrice();
        this.grade = memberEntity.getGrade();
    }
}
