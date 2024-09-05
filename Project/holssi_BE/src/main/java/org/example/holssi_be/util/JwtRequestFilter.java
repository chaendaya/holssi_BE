package org.example.holssi_be.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.exception.InvalidTokenException;
import org.example.holssi_be.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    // JwtRequestFilter:
    // JWT 토큰을 검증하고 사용자 인증을 설정한다.
    // 인증 실패 시 적절한 에러 응답을 반환한다.

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/",
            "/index.html",
            "/favicon.ico",
            "/api/auth/.*",
            "/api/login",
            "/api/admin/create",
            "/h2-console",
            "/h2-console/.*",
            "/api/temp/.*",
            "/api/garbages/getValue"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // JWT 필터를 적용하지 않을 경로 확인
        String requestURI = request.getRequestURI();
        if (shouldExclude(requestURI)) {
            // 필터를 적용하지 않고 요청을 다음 필터로 전달
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");

        // 요청 헤더에서 JWT 토큰 추출
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                // JWT 토큰에서 사용자 이름(이메일) 추출
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                // 사용자 이름이 존재하고 현재 인증되지 않은 상태라면
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 사용자 세부 정보 로드
                    UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

                    // JWT 토큰 유효성 검증
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        // 인증 토큰 생성 및 세부 정보 설정
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // SecurityContext 에 인증 설정
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                // JWT 토큰 검증 실패 시 InvalidTokenException 발생
                throw new InvalidTokenException("Unable to get JWT Token", e);
            } catch (ExpiredJwtException e) {
                // JWT 토큰 만료 시 InvalidTokenException 발생
                throw new InvalidTokenException("JWT Token has expired", e);
            } catch (MalformedJwtException e) {
                // JWT 토큰 형식 오류 시 InvalidTokenException 발생
                throw new InvalidTokenException("JWT Token is malformed", e);
            } catch (UnsupportedJwtException e) {
                // JWT 토큰 지원되지 않음 시 InvalidTokenException 발생
                throw new InvalidTokenException("JWT Token is unsupported", e);
            } catch (SignatureException e) {
                // JWT 서명 유효하지 않음 시 InvalidTokenException 발생
                throw new InvalidTokenException("JWT signature is invalid", e);
            }
        } else {
            // Bearer 문자열이 없는 JWT 토큰 형식 오류 시 InvalidTokenException 발생
            throw new InvalidTokenException("JWT Token does not begin with Bearer String");
        }
        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }

    private boolean shouldExclude(String requestURI) {
        // `/` 경로는 반드시 제외되도록 수정
        if ("/".equals(requestURI)) {
            return true;
        }
        return EXCLUDED_PATHS.stream().anyMatch(pattern -> requestURI.matches(pattern));
    }
}
