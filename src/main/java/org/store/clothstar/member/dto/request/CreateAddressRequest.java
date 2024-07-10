package org.store.clothstar.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.vo.AddressInfo;

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

    @NotBlank(message = "상세 주소를 입력해 주세요.")
    private String addressDetail;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 전화번호 형식입니다.")
    private String telNo;
    private String deliveryRequest;
    private boolean defaultAddress;

    public Address toAddress(Member member) {
        AddressInfo addressInfo = AddressInfo.builder()
                .addressBasic(this.addressBasic)
                .addressDetail(this.addressDetail)
                .zipNo(this.zipNo)
                .deliveryRequest(this.deliveryRequest)
                .build();

        return Address.builder()
                .receiverName(this.receiverName)
                .member(member)
                .telNo(this.telNo)
                .addressInfo(addressInfo)
                .defaultAddress(this.defaultAddress)
                .build();
    }
}
