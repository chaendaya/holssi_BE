package org.example.holssi_be.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.CollectorDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.service.TempService;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/temp")
@RequiredArgsConstructor
public class TempController {

    // 테스트를 위해 인증없이 User와 Collector를 추가하는 임시 API 입니다.

    private final TempService tempService;

    @PostMapping("/user")
    public ResponseDTO createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        try {
            tempService.createUser(userDTO);
            return new ResponseDTO(true, "User created successfully", null);
        } catch (Exception e) {
            return new ResponseDTO(false, null, e.getMessage());
        }
    }

    @PostMapping("/collector")
    public ResponseDTO createCollector(@RequestBody @Valid CollectorDTO collectorDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        try {
            tempService.createCollector(collectorDTO);
            return new ResponseDTO(true, "Collector created successfully", null);
        } catch (Exception e) {
            return new ResponseDTO(false, null, e.getMessage());
        }
    }

}
