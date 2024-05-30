package org.store.clothstar.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.member.domain.Seller;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "seller")
public class SellerEntity extends BaseEntity {
    @Id
    private Long memberId;
    @Column(unique = true)
    private String brandName;
    @Column(unique = true)
    private String bizNo;
    private int totalSellPrice;

    @OneToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    public SellerEntity(Seller seller) {
        this.memberId = seller.getMemberId();
        this.brandName = seller.getBrandName();
        this.bizNo = seller.getBizNo();
        this.totalSellPrice = seller.getTotalSellPrice();
    }
}
