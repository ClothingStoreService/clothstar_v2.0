package org.store.clothstar.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Account;
import org.store.clothstar.member.domain.Authorization;
import org.store.clothstar.member.domain.CustomUserDetails;
import org.store.clothstar.member.repository.AccountRepository;
import org.store.clothstar.member.repository.AuthorizationRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AuthorizationRepository authorizationRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername() 실행");
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다."));

        Authorization authorization = authorizationRepository.findById(account.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("권한을 찾을수 없습니다."));

        return new CustomUserDetails(account, authorization);
    }
}