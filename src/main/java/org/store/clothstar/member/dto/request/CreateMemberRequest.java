package org.store.clothstar.member.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberRole;
import org.store.clothstar.member.domain.vo.MemberShoppingActivity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateMemberRequest {
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 비어 있을 수 없습니다.")
    private String name;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 전화번호 형식입니다.")
    private String telNo;

    @NotNull(message = "인증번호를 입력해 주세요")
    private String certifyNum;

    public Member toMember(String encryptedPassword) {
        return Member.builder()
                .email(email)
                .password(encryptedPassword)
                .name(name)
                .telNo(telNo)
                .role(MemberRole.USER)
                .memberShoppingActivity(new MemberShoppingActivity().init())
                .build();
    }

    public Member toMember() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .telNo(telNo)
                .role(MemberRole.USER)
                .memberShoppingActivity(new MemberShoppingActivity().init())
                .build();
    }
}
