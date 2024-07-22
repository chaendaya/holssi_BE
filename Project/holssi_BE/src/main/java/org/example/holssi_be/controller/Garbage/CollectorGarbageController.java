package org.example.holssi_be.controller.Garbage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.holssi_be.dto.Garbage.AcceptGarbageDTO;
import org.example.holssi_be.dto.Garbage.GarbageDetailsDTO;
import org.example.holssi_be.dto.Garbage.GarbageInfoDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.service.Garbage.CollectorGarbageService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garbages")
public class CollectorGarbageController {

    private final CollectorGarbageService collectorGarbageService;

    @Autowired
    public CollectorGarbageController(CollectorGarbageService collectorGarbageService) {
        this.collectorGarbageService = collectorGarbageService;
    }

    // 매칭 대기 중인 쓰레기 리스트 조회 - Collector
    @GetMapping("/waiting")
    public ResponseDTO getWaitingGarbages(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        String location = member.getCollector().getLocation();
        List<GarbageInfoDTO> waitingGarbages = collectorGarbageService.getWaitingGarbages(location);

        return new ResponseDTO(true, waitingGarbages, null);
    }

    // 수거할 날짜 등록 및 매칭 완료 - Collector
    @PostMapping("/{garbage-id}/accept")
    public ResponseDTO acceptGarbage(@PathVariable("garbage-id") Long garbageId, @RequestBody @Valid AcceptGarbageDTO acceptGarbageDTO,
                                     BindingResult bindingResult, HttpServletRequest request) {
        ValidationUtil.validateRequest(bindingResult);
        Member member = (Member) request.getAttribute("member");
        collectorGarbageService.acceptGarbage(garbageId, acceptGarbageDTO, member);
        return new ResponseDTO(true, "Garbage accepted and collection date set", null);
    }

    // 수거인이 수락한 쓰레기 리스트 조회 - Collector
    @GetMapping("/accepted")
    public ResponseDTO getAcceptedGarbages(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        List<GarbageInfoDTO> acceptedGarbages = collectorGarbageService.getAcceptedGarbages(member);
        return new ResponseDTO(true, acceptedGarbages, null);
    }

    // 개별 쓰레기 정보 조회 - Collector
    @GetMapping("/{garbage-id}")
    public ResponseDTO getGarbageDetails(@PathVariable("garbage-id") Long garbageId) {
        GarbageDetailsDTO garbageDetails = collectorGarbageService.getGarbageDetails(garbageId);
        return new ResponseDTO(true, garbageDetails, null);
    }
}