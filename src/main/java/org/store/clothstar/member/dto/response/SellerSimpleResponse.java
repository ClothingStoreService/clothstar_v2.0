package org.store.clothstar.member.dto.response;

import lombok.*;
import org.store.clothstar.member.domain.Seller;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerSimpleResponse {
    private Long memberId;
    private String brandName;
    private String bizNo;

    public static SellerSimpleResponse from(Seller seller) {
        return SellerSimpleResponse.builder()
                .memberId(seller.getMemberId())
                .brandName(seller.getBrandName())
                .bizNo(seller.getBizNo())
                .build();
    }
}
