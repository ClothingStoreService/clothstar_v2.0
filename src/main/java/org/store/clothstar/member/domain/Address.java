package org.store.clothstar.member.domain;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.member.entity.AddressEntity;

@Getter
@Builder
public class Address {
    private Long addressId;
    private Long memberId;
    private String receiverName;
    private String zipNo;
    private String addressBasic;
    private String addressDetail;
    private String telNo;
    private String deliveryRequest;
    private boolean defaultAddress;

    public Address(AddressEntity addressEntity) {
        this.addressId = addressEntity.getAddressId();
        this.memberId = addressEntity.getMember().getMemberId();
        this.receiverName = addressEntity.getReceiverName();
        this.zipNo = addressEntity.getZipNo();
        this.addressBasic = addressEntity.getAddressBasic();
        this.addressDetail = addressEntity.getAddressDetail();
        this.telNo = addressEntity.getTelNo();
        this.deliveryRequest = addressEntity.getDeliveryRequest();
        this.defaultAddress = addressEntity.isDefaultAddress();
    }
}
