package org.example.holssi_be.controller.Garbage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.holssi_be.dto.Garbage.RegisterGarbageDTO;
import org.example.holssi_be.dto.Garbage.RegisteredGarbageDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.service.Garbage.UserGarbageService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garbages")
public class UserGarbageController {

    private final UserGarbageService userGarbageService;

    @Autowired
    public UserGarbageController(UserGarbageService userGarbageService) {
        this.userGarbageService = userGarbageService;
    }

    @PostMapping("/totalWeight")
    public ResponseDTO totalWeight(@RequestBody @Valid RegisterGarbageDTO registerGarbageDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);
        double totalWeight = registerGarbageDTO.getOrganicWeight() + registerGarbageDTO.getNon_organicWeight();

        return new ResponseDTO(true, totalWeight, null);
    }

    // 쓰레기 등록 - User
    @PostMapping("/register")
    public ResponseDTO registerGarbage(@RequestBody @Valid RegisterGarbageDTO registerGarbageDTO, BindingResult bindingResult,
                                       HttpServletRequest request) {
        ValidationUtil.validateRequest(bindingResult);
        Member member = (Member) request.getAttribute("member");
        userGarbageService.registerGarbage(registerGarbageDTO, member);

        return new ResponseDTO(true, "Garbage register completed", null);
    }

    // 등록한 쓰레기 중 수거인 매칭된 쓰레기 리스트 조회 - User
    @GetMapping("/registered")
    public ResponseDTO getRegisteredGarbages(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        Long userId = member.getId();
        List<RegisteredGarbageDTO> registeredGarbages = userGarbageService.getRegisteredGarbages(userId);
        return new ResponseDTO(true, registeredGarbages, null);
    }

    // 개별 쓰레기 수거인 정보
}
