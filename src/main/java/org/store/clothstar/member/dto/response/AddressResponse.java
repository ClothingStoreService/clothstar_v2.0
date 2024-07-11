package org.store.clothstar.member.dto.response;

import lombok.Getter;
import org.store.clothstar.member.domain.Address;


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

    public AddressResponse(Address address) {
        this.memberId = address.getMember().getMemberId();
        this.receiverName = address.getReceiverName();
        this.zipNo = address.getAddressInfo().getZipNo();
        this.addressBasic = address.getAddressInfo().getAddressBasic();
        this.addressDetail = address.getAddressInfo().getAddressDetail();
        this.telNo = address.getTelNo();
        this.deliveryRequest = address.getAddressInfo().getDeliveryRequest();
        this.defaultAddress = address.isDefaultAddress();
    }
}