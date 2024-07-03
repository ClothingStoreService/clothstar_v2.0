package org.store.clothstar.member.dto.response;

import lombok.Getter;
import org.store.clothstar.member.entity.SellerEntity;

import java.time.LocalDateTime;

@Getter
public class SellerResponse {
    private Long memberId;
    private String brandName;
    private String bizNo;
    private int totalPaymentPrice;
    private LocalDateTime createdAt;

    public SellerResponse(SellerEntity sellerEntity) {
        this.memberId = sellerEntity.getMemberId();
        this.brandName = sellerEntity.getBrandName();
        this.bizNo = sellerEntity.getBizNo();
        this.totalPaymentPrice = sellerEntity.getTotalSellPrice();
        this.createdAt = sellerEntity.getCreatedAt();
    }
}
