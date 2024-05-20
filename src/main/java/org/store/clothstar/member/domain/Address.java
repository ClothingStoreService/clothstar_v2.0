package org.store.clothstar.member.domain;

import lombok.Builder;
import lombok.Getter;

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
}
