package org.example.holssi_be.controller;

import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.exception.InvalidTokenFormatException;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/home")
    public ResponseEntity<ResponseDTO> getHome(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new InvalidTokenFormatException("Invalid token format");
        }

        // Bearer 토큰 파싱
        String jwtToken = token.substring(7);

        // JWT 토큰에서 사용자 ID 추출
        String email = jwtTokenUtil.getUsernameFromToken(jwtToken);
        //Long id = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // 사용자 정보 로드
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with email: " + email));

        // 응답 구성
        Map<String, Object> response = new HashMap<>();
        response.put("role", member.getRole());
        response.put("name", member.getName());

        return ResponseEntity.ok(new ResponseDTO(true, response));
    }
}
