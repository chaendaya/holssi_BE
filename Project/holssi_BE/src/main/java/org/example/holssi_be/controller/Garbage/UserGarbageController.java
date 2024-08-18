package org.example.holssi_be.controller.Garbage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.RegisterGarbageDTO;
import org.example.holssi_be.dto.Garbage.RegisteredGarbageDTO;
import org.example.holssi_be.dto.Garbage.RegisteredResponseDTO;
import org.example.holssi_be.dto.LocationDTO;
import org.example.holssi_be.dto.RatingRequestDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.service.Garbage.UserGarbageService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        return new ResponseDTO(true, totalValue);
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
    public ResponseDTO getRegisteredGarbages(@RequestParam(defaultValue = "1") int page, HttpServletRequest request) {

        // 페이지 번호를 0 기반으로 변환 (클라이언트의 1페이지 -> 내부 0페이지)
        int zeroBasePage = page -1;

        Member member = (Member) request.getAttribute("member");
        Page<RegisteredGarbageDTO> registeredGarbagesPage = userGarbageService.getRegisteredGarbages(member, zeroBasePage);

        RegisteredResponseDTO registeredDTO = new RegisteredResponseDTO(
                registeredGarbagesPage.getContent(),
                registeredGarbagesPage.hasNext() ? page +1 : -1,
                registeredGarbagesPage.isLast()
        );
        return new ResponseDTO(true, registeredDTO);
    }

    // 개별 쓰레기 수거인 정보
    @GetMapping("/{garbage-id}/collectorInfo")
    public ResponseDTO getGarbageDetails(@PathVariable("garbage-id") Long garbageId, HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        String collectorName = userGarbageService.getCollectorInfo(garbageId, member);

        return new ResponseDTO(true, collectorName);
    }

    // 개별 쓰레기 수거인 실시간 위치
    @GetMapping("/{garbage-id}/location")
    public ResponseDTO getLocation(@PathVariable("garbage-id") Long garbageId, HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        LocationDTO location = userGarbageService.getLocation(garbageId, member);

        return new ResponseDTO(true, location);
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
