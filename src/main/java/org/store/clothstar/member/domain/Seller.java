package org.store.clothstar.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.member.dto.request.CreateSellerRequest;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "seller")
public class Seller extends BaseEntity {
    @Id
    private Long memberId;

    @Column(unique = true)
    private String brandName;
    @Column(unique = true)
    private String bizNo;

    private int totalSellPrice;

    @OneToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    public Seller(CreateSellerRequest createSellerRequest, Member member) {
        this.brandName = createSellerRequest.getBrandName();
        this.bizNo = createSellerRequest.getBizNo();
        this.member = member;
    }
}
