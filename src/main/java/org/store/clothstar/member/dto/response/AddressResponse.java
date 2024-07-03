package org.store.clothstar.member.dto.response;

import lombok.Getter;
import org.store.clothstar.member.entity.AddressEntity;

@Getter
public class AddressResponse {
    private Long memberId;
    private String receiverName;
    private String zipNo;
    private String addressBasic;
    private String addressDetail;
    private String telNo;
    private String deliveryRequest;
    private boolean defaultAddress;

    public AddressResponse(AddressEntity addressEntity) {
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