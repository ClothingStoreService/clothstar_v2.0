package org.store.clothstar.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.store.clothstar.member.domain.Address;

import jakarta.validation.constraints.NotBlank;

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
                .receiverName(receiverName)
                .zipNo(zipNo)
                .addressBasic(addressBasic)
                .addressDetail(addressDetail)
                .telNo(telNo)
                .deliveryRequest(deliveryRequest)
                .defaultAddress(defaultAddress)
                .build();
    }
}
