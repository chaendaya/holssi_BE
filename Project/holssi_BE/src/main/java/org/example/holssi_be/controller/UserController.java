package org.example.holssi_be.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;

import org.example.holssi_be.service.Garbage.UserGarbageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserGarbageService userGarbageService;

    // 저축된 총 RP값 조회
    @GetMapping("/savings")
    public ResponseDTO getTotalRP(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        Long userId = member.getId();
        double totalRp = userGarbageService.getTotalRp(userId, member);
        return new ResponseDTO(true, totalRp,null);
    }
}
