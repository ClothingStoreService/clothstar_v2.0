package org.store.clothstar.order.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    @Column(name = "fixed_price")
    private int fixedPrice; // 고정된 상품 가격 ( 주문 당시 가격 )
    @Column(name = "onekind_total_price")
    private int oneKindTotalPrice; // 상품 종류 하나당 총 가격
}
