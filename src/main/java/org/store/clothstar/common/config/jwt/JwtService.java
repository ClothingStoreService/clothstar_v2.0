package org.store.clothstar.common.config.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.NotFoundMemberException;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.MemberRepository;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public String getRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public String getAccessTokenByRefreshToken(String refreshToken) {
        Long memberId = jwtUtil.getMemberId(refreshToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        return jwtUtil.createAccessToken(member);
    }
}
