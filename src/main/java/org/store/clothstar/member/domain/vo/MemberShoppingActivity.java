package org.store.clothstar.member.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.store.clothstar.member.domain.MemberGrade;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberShoppingActivity {
    @ColumnDefault(value = "0")
    private int totalPaymentPrice;
    @ColumnDefault(value = "0")
    private int point;
    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    public MemberShoppingActivity init() {
        return MemberShoppingActivity.builder()
                .totalPaymentPrice(0)
                .point(0)
                .grade(MemberGrade.BRONZE)
                .build();
    }
}
