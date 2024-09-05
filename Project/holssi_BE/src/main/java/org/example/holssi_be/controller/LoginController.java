package org.example.holssi_be.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.LoginDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.service.CustomUserDetailsService;
import org.example.holssi_be.util.JwtTokenUtil;
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

            // JWT 액세스 토큰 생성
            final String accessToken = jwtTokenUtil.createToken(userDetails);

            // 응답 헤더에 액세스 토큰 추가
            response.setHeader("Authorization", "Bearer " + accessToken);

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
}