package org.store.clothstar.member.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.member.entity.SellerEntity;

@Getter
@Builder
public class Seller {
	private Long memberId;
	private String brandName;
	private String bizNo;
	private int totalSellPrice;
	private LocalDateTime createdAt;

	public Seller(SellerEntity sellerEntity) {
		this.memberId = sellerEntity.getMemberId();
		this.brandName = sellerEntity.getBrandName();
		this.bizNo = sellerEntity.getBizNo();
		this.totalSellPrice = sellerEntity.getTotalSellPrice();
		this.createdAt = sellerEntity.getCreatedAt();
	}
}
