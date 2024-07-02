package org.store.clothstar.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final MemberEntity memberEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + String.valueOf(memberEntity.getRole())));
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        //true -> 계정 만료되지 않았음
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //true -> 계정 잠금되지 않음
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //true -> 패스워드 만료 되지 않음
        return true;
    }

    @Override
    public boolean isEnabled() {
        //ture -> 계정 사용 가능
        return true;
    }
}