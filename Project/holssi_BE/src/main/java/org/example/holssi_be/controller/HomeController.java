package org.example.holssi_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.exception.MemberNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public ResponseDTO getHome(HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        if (member == null) {
            throw new MemberNotFoundException();
        }

        // 응답 구성
        Map<String, Object> response = new HashMap<>();
        response.put("role", member.getRole());
        response.put("name", member.getName());

        return new ResponseDTO(true, response);
    }
}