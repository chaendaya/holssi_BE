package org.example.holssi_be.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.AdminDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.service.AdminService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

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
}
