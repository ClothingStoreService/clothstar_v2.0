package org.store.clothstar.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberLoginRequest {
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    String email;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    String password;
}
