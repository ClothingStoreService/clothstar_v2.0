package org.store.clothstar.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateAddressRequest {
    @NotBlank(message = "받는 사람 이름은 비어 있을 수 없습니다.")
    private String receiverName;

    @NotBlank(message = "우편번호는 비어 있을 수 없습니다.")
    private String zipNo;

    @NotBlank(message = "기본 주소는 비어 있을 수 없습니다.")
    private String addressBasic;
    private String addressDetail;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 전화번호 형식입니다.")
    private String telNo;
    private String deliveryRequest;
    private boolean defaultAddress;

    public Address toAddress(Member member) {
        return Address.builder()
                .receiverName(this.receiverName)
                .member(member)
                .zipNo(this.zipNo)
                .addressBasic(this.addressBasic)
                .addressDetail(this.addressDetail)
                .telNo(this.telNo)
                .deliveryRequest(this.deliveryRequest)
                .defaultAddress(this.defaultAddress)
                .build();
    }
}
