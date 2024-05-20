package org.store.clothstar.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.store.clothstar.member.domain.Seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateSellerRequest {
    @NotBlank(message = "브랜드 이름은 필수 입력값 입니다.")
    private String brandName;

    @Pattern(regexp = "([0-9]{3})-?([0-9]{2})-?([0-9]{5})", message = "유효하지 않은 사업자 번호 형식입니다.")
    private String bizNo;

    public Seller toSeller(Long memberId) {
        return Seller.builder()
                .memberId(memberId)
                .brandName(brandName)
                .bizNo(bizNo)
                .totalSellPrice(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}