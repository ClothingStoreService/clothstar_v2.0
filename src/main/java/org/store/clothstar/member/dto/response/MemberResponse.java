package org.store.clothstar.member.dto.response;

import lombok.Getter;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberGrade;

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
        this.totalPaymentPrice = member.getMemberShoppingActivity().getTotalPaymentPrice();
        this.grade = member.getMemberShoppingActivity().getGrade();
    }
}
