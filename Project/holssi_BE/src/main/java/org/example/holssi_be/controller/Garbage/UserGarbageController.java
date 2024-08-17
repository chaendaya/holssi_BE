package org.example.holssi_be.controller.Garbage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.RegisterGarbageDTO;
import org.example.holssi_be.dto.Garbage.RegisteredGarbageDTO;
import org.example.holssi_be.dto.LocationDTO;
import org.example.holssi_be.dto.RatingRequestDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.service.Garbage.UserGarbageService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garbages")
@RequiredArgsConstructor
public class UserGarbageController {

    private final UserGarbageService userGarbageService;

    @PostMapping("/totalValue")
    public ResponseDTO totalWeight(@RequestBody @Valid RegisterGarbageDTO registerGarbageDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        double organicWeight = registerGarbageDTO.getOrganicWeight();
        double non_organicWeight = registerGarbageDTO.getNon_organicWeight();
        double totalValue = 60 * organicWeight + 80 * non_organicWeight;

        return new ResponseDTO(true, totalValue, null);
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
        List<RegisteredGarbageDTO> registeredGarbages = userGarbageService.getRegisteredGarbages(member);

        return new ResponseDTO(true, registeredGarbages, null);
    }

    // 개별 쓰레기 수거인 정보
    @GetMapping("/{garbage-id}/collectorInfo")
    public ResponseDTO getGarbageDetails(@PathVariable("garbage-id") Long garbageId, HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        String collectorName = userGarbageService.getCollectorInfo(garbageId, member);

        return new ResponseDTO(true, collectorName, null);
    }

    // 개별 쓰레기 수거인 실시간 위치
    @GetMapping("/{garbage-id}/location")
    public ResponseDTO getLocation(@PathVariable("garbage-id") Long garbageId, HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        LocationDTO location = userGarbageService.getLocation(garbageId, member);

        return new ResponseDTO(true, location, null);
    }


    // 개별 쓰레기 수거인 평가
    @PostMapping("/{garbage-id}/rating")
    public ResponseDTO rateCollector(@PathVariable("garbage-id") Long garbageId, @RequestBody @Valid RatingRequestDTO ratingRequestDTO, HttpServletRequest request) {
        Integer score = ratingRequestDTO.getScore();
        if (score == null || score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }

        Member member = (Member) request.getAttribute("member");

        userGarbageService.rateCollector(garbageId, member, score);
        return new ResponseDTO(true, "Rating completed !", null);
    }
}
