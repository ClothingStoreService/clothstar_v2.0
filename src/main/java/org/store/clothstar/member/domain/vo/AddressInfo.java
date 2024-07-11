package org.store.clothstar.member.domain.vo;

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
public class AddressInfo {
    private String addressBasic;
    private String addressDetail;
    private String zipNo;
    private String deliveryRequest;
}
