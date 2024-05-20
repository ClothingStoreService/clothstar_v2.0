package org.store.clothstar.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberGrade;
import org.store.clothstar.member.domain.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    public Member toMember(String encryptedPassword) {
        return Member.builder()
                .email(email)
                .password(encryptedPassword)
                .name(name)
                .telNo(telNo)
                .totalPaymentPrice(0)
                .point(0)
                .role(MemberRole.USER)
                .grade(MemberGrade.BRONZE)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
