package org.example.holssi_be.controller;

import jakarta.validation.Valid;
import org.example.holssi_be.dto.AdminDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.service.AdminService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
