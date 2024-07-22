package org.example.holssi_be.controller.Garbage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.holssi_be.dto.Garbage.RegisterGarbageDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.service.Garbage.UserGarbageService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*@GetMapping("/registered")
    public ResponseDTO getRegisteredGarbages(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        Long user_id = member.getId();
        List<RegisteredGarbageDTO> registeredGarbages = userGarbageService.getRegisteredGarbages(user_id);


    }*/
    // 등록한 쓰레기 중 수거인 매칭된 쓰레기 리스트 조회 - User
    // 담당 수거인 이름
    // 담당 수거인이 입력한 수거날짜 -> 요일 변환
    // 해당 쓰레기의 수거 완료 여부

    // 개별 쓰레기 수거인 정보
}
