package org.example.holssi_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.CollectingGarbageDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.service.Garbage.CollectorGarbageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collector")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorGarbageService collectorGarbageService;

    // 수거인이 수거 중인 쓰레기 Id 조회
    @GetMapping("/garbageId")
    public ResponseDTO getCollectingGarbageId(HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        List<CollectingGarbageDTO> response = collectorGarbageService.getCollectingGarbageId(member);

        return new ResponseDTO(true, response, null);
    }
}
