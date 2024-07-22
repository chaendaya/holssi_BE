package org.example.holssi_be.interceptor;

import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.exception.InvalidTokenFormatException;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new InvalidTokenFormatException("Invalid token format");
        }

        // Bearer 토큰 파싱
        String jwtToken = token.substring(7);

        // JWT 토큰에서 사용자 이메일 추출
        String email = jwtTokenUtil.getUsernameFromToken(jwtToken);

        // 사용자 정보 로드
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with email: " + email));

        // 요청 속성에 멤버 정보 추가
        request.setAttribute("member", member);
        return true;
    }

}
