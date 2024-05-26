package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.CustomUserDetails;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.MemberMybatisRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailsService implements UserDetailsService {
    private final MemberMybatisRepository memberMybatisRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername() 실행");
        Member member = memberMybatisRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));

        return new CustomUserDetails(member);
    }
}