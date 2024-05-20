package org.store.clothstar.member.dto.response;

import org.store.clothstar.member.domain.Address;

import lombok.Getter;

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
		this.memberId = address.getMemberId();
		this.receiverName = address.getReceiverName();
		this.zipNo = address.getZipNo();
		this.addressBasic = address.getAddressBasic();
		this.addressDetail = address.getAddressDetail();
		this.telNo = address.getTelNo();
		this.deliveryRequest = address.getDeliveryRequest();
		this.defaultAddress = address.isDefaultAddress();
	}
}