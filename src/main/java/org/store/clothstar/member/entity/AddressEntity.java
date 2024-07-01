package org.store.clothstar.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.store.clothstar.member.domain.Address;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String receiverName;
    private String zipNo;
    private String addressBasic;
    private String addressDetail;
    private String telNo;
    private String deliveryRequest;
    private boolean defaultAddress;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    public AddressEntity(Address address) {
        this.addressId = address.getAddressId();
        this.receiverName = address.getReceiverName();
        this.zipNo = address.getZipNo();
        this.addressBasic = address.getAddressBasic();
        this.addressDetail = address.getAddressDetail();
        this.telNo = address.getTelNo();
        this.deliveryRequest = address.getDeliveryRequest();
        this.defaultAddress = address.isDefaultAddress();
    }
}