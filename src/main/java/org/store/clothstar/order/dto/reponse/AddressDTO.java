package org.store.clothstar.order.dto.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "배송지 정보")
public class AddressDTO {
    @Schema(description = "수령인 이름", example = "수빈")
    private String receiverName;

    @Schema(description = "기본 주소", example = "서울시 강남구")
    private String addressBasic;

    @Schema(description = "상세 주소", example = "123-456")
    private String addressDetail;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String telNo;

    @Schema(description = "배송 요청 사항", example = "문 앞에 놓아주세요.")
    private String deliveryRequest;
}
