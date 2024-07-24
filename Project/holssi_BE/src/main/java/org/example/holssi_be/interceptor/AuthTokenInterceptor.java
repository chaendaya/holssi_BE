package org.example.holssi_be.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.util.JwtTokenUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthTokenInterceptor implements HandlerInterceptor {
    // AuthTokenInterceptor:
    // 요청이 처리되기 전에 JWT 토큰을 검사하여 사용자 정보를 request 속성에 추가한다.
    // JwtRequestFilter 에서 이미 인증 처리가 되어 있으므로, 인터셉터는 사용자 정보를 추가하는 역할을 한다.

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;

    // preHandle 메서드가 직접적으로 호출되는 것이 아니라, 스프링 프레임워크가 인터셉터를 자동으로 호출
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 요청 헤더에서 JWT 토큰 추출
        String token = request.getHeader("Authorization");

        // Bearer 토큰 형식 확인 및 파싱
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            // JWT 토큰에서 사용자 이메일 추출
            String email = jwtTokenUtil.getUsernameFromToken(jwtToken);

            // 이메일이 존재하면 사용자 정보 로드
            if (email != null) {
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Member not found with email: " + email));

                // 요청 속성에 멤버 정보 추가
                request.setAttribute("member", member);
            }
        }
        // 요청을 계속 처리
        return true;
    }
}
