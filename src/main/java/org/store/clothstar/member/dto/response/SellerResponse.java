package org.store.clothstar.member.dto.response;

import lombok.Getter;
import org.store.clothstar.member.domain.Seller;

import java.time.LocalDateTime;

@Getter
public class SellerResponse {
    private Long memberId;
    private String brandName;
    private String bizNo;
    private int totalPaymentPrice;
    private LocalDateTime createdAt;

    public SellerResponse(Seller seller) {
        this.memberId = seller.getMemberId();
        this.brandName = seller.getBrandName();
        this.bizNo = seller.getBizNo();
        this.totalPaymentPrice = seller.getTotalSellPrice();
        this.createdAt = seller.getCreatedAt();
    }
}
