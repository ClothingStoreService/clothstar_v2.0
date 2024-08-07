package org.store.clothstar.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.order.domain.type.ApprovalStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "(판매자)주문 수정용 Request")
public class OrderSellerRequest {

    @Schema(description = "요청 주문 상태(승인 or 취소)")
    @NotNull(message = "요청할 주문 상태를 입력해주세요.")
    private ApprovalStatus approvalStatus;
}
