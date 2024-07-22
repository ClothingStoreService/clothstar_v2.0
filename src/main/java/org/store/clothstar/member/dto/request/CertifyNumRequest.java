package org.store.clothstar.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CertifyNumRequest {
    @NotNull(message = "이메일을 입력해 주세요")
    private String email;
}