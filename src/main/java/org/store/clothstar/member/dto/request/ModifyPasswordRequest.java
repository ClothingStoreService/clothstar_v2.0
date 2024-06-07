package org.store.clothstar.member.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyPasswordRequest {
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    String password;
}
