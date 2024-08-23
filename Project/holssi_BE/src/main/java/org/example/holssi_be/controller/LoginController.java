package org.example.holssi_be.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.LoginDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.exception.IllegalException;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.service.CustomUserDetailsService;
import org.example.holssi_be.util.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping ("/api")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private final MemberRepository memberRepository;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> createAuthToken(@RequestBody @Valid LoginDTO loginDTO,  HttpServletResponse response) {
        try {
            // Member 인증
            authenticate(loginDTO.getEmail(), loginDTO.getPassword());

            // UserDetails 정보 로드
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());

            // Member 정보 조회
            Member member = memberRepository.findByEmail(loginDTO.getEmail()).get();

            // JWT 액세스 토큰 및 리프레시 토큰 생성
            final String accessToken = jwtTokenUtil.createToken(userDetails);
            final String refreshToken = jwtTokenUtil.createRefreshToken(userDetails);

            // 응답 헤더에 액세스 토큰 추가
            response.setHeader("Authorization", "Bearer " + accessToken);

            // 리프레시 토큰을 쿠키에 추가 (SameSite=None)
            String setCookieHeader = String.format("refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=None",
                    refreshToken,
                    Math.toIntExact(jwtTokenUtil.getRefreshTokenExpirationSeconds()));
            response.addHeader("Set-Cookie", setCookieHeader);

            // 응답 바디 구성
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("role", member.getRole());

            return ResponseEntity.ok(new ResponseDTO(true, responseBody, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, null, "Invalid credentials"));
        }
    }

    private void authenticate(String email, String password) {
        // 사용자의 사용자 이름과 비밀번호를 포함하는 인증 토큰
        // AuthenticationManager에 의해 처리되어 사용자의 자격 증명을 확인하는 데 사용된다.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDTO> refreshAuthToken(HttpServletRequest request) {
        try {
            // 쿠키에서 리프레시 토큰 추출
            Cookie[] cookies = request.getCookies();
            String refreshToken = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken == null) {
                throw new IllegalException("Refresh token is missing");
            }

            // 헤더에서 만료된 액세스 토큰 추출
            String expiredAccessToken = request.getHeader("Authorization");
            if (expiredAccessToken == null || !expiredAccessToken.startsWith("Bearer ")) {
                throw new IllegalException("Invalid access token format");
            }
            expiredAccessToken = expiredAccessToken.substring(7); // "Bearer " 제거

            // 만료된 토큰에서 사용자 정보 추출 (예외 발생하지 않도록 처리)
            String username;
            try {
                username = jwtTokenUtil.getUsernameFromToken(expiredAccessToken);
            } catch (ExpiredJwtException e) {
                username = e.getClaims().getSubject(); // 만료된 토큰에서도 subject (username) 추출 가능
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 리프레시 토큰 검증
            if (jwtTokenUtil.validateToken(refreshToken, userDetails)) {
                // 새 액세스 토큰 생성
                final String newAccessToken = jwtTokenUtil.createToken(userDetails);

                // 새 액세스 토큰을 응답 헤더에 추가
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + newAccessToken);

                return ResponseEntity.ok().headers(headers).body(new ResponseDTO(true, "Access token refreshed", null));
            } else {
                throw new IllegalException("Refresh token is invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(false, null, e.getMessage()));
        }
    }
}