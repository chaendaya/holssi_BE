package org.example.holssi_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.AdminDTO;
import org.example.holssi_be.dto.Garbage.GarbageInfoDTO;
import org.example.holssi_be.dto.Garbage.GarbageValueDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.entity.domain.GarbageValue;
import org.example.holssi_be.service.AdminService;
import org.example.holssi_be.service.Garbage.AdminGarbageService;
import org.example.holssi_be.service.GarbageValueService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminGarbageService adminGarbageService;
    private final GarbageValueService garbageValueService;

    // admin 생성
    @PostMapping("/create")
    public ResponseDTO createAdmin(@RequestBody @Valid AdminDTO adminDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        try {
            adminService.createAdmin(adminDTO);
            return new ResponseDTO(true, "Admin created successfully", null);
        } catch (Exception e) {
            return new ResponseDTO(false, null, e.getMessage());
        }
    }

    // 매칭되지 않은 전 지역 쓰레기 조회 - admin
    @GetMapping("/status")
    public ResponseDTO getAllWaitingGarbages(HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        List<GarbageInfoDTO> allWaitingGarbages = adminGarbageService.getAllWaitingGarbages(member);
        return new ResponseDTO(true, allWaitingGarbages, null);
    }

    // Rp 업데이트
    @PostMapping("/updateValue")
    public ResponseDTO updateValue(@RequestBody @Valid GarbageValueDTO garbageValueDTO, HttpServletRequest request, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        Member member = (Member) request.getAttribute("member");
        GarbageValue garbageValue = garbageValueService.updateValue(member, garbageValueDTO);
        Long id = garbageValue.getId();

        return new ResponseDTO(true, "Garbages Value " + id + " is updated", null);
    }
}
