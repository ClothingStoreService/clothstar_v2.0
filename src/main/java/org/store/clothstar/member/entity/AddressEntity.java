package org.store.clothstar.member.entity;

import jakarta.persistence.*;
import lombok.*;

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
}