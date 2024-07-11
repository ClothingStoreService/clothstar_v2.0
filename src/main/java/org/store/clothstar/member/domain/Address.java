package org.store.clothstar.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.store.clothstar.member.domain.vo.AddressInfo;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String receiverName;
    private String telNo;
    private boolean defaultAddress;

    @Embedded
    AddressInfo addressInfo;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}