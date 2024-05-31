package org.store.clothstar.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateAddressRequest {
    @NotBlank(message = "받는 사람 이름은 비어 있을 수 없습니다.")
    private String receiverName;

    @NotBlank(message = "우편번호는 비어 있을 수 없습니다.")
    private String zipNo;

    @NotBlank(message = "기본 주소는 비어 있을 수 없습니다.")
    private String addressBasic;
    private String addressDetail;

    @NotBlank(message = "전화번호는 비어 있울 수 없습니다.")
    private String telNo;
    private String deliveryRequest;
    private boolean defaultAddress;

    public Address toAddress(Long memberId) {
        return Address.builder()
                .memberId(memberId)
                .receiverName(this.receiverName)
                .zipNo(this.zipNo)
                .addressBasic(this.addressBasic)
                .addressDetail(this.addressDetail)
                .telNo(this.telNo)
                .deliveryRequest(this.deliveryRequest)
                .defaultAddress(this.defaultAddress)
                .build();
    }

    public AddressEntity toAddressEntity(MemberEntity memberEntity) {
        return AddressEntity.builder()
                .receiverName(this.receiverName)
                .member(memberEntity)
                .zipNo(this.zipNo)
                .addressBasic(this.addressBasic)
                .addressDetail(this.addressDetail)
                .telNo(this.telNo)
                .deliveryRequest(this.deliveryRequest)
                .defaultAddress(this.defaultAddress)
                .build();
    }
}
