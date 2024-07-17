package org.example.holssi_be.controller;

import org.example.holssi_be.dto.AuthDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.service.AuthService;
import org.example.holssi_be.service.EmailService;
import org.example.holssi_be.service.UserService;
import org.example.holssi_be.service.WhatsAppService;
import org.example.holssi_be.util.AuthUtil;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseDTO registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        Map<String, String> userData = AuthUtil.createTemporaryUser(userDTO);
        authService.saveTemporaryUser(userDTO.getUserEmail(), userData);
        return new ResponseDTO(true, null, null);
    }

    @PostMapping("/sendEmail")
    public ResponseDTO sendEmail(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = AuthUtil.getTemporaryUser(authDTO.getPrimaryKey(), authService);
        String email = (String) userData.get("userEmail");
        String code = authService.generateCode();

        emailService.sendEmail(email, code);
        authService.saveCode(email, code);
        return new ResponseDTO(true, "Verification code sent via email.", null);
    }

    @PostMapping("/sendWhatsApp")
    public ResponseDTO sendWhatsApp(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = AuthUtil.getTemporaryUser(authDTO.getPrimaryKey(), authService);
        String phone = (String) userData.get("phone");
        String code = authService.generateCode();

        whatsAppService.sendWhatsApp(phone, code);
        authService.saveCode(phone, code);
        return new ResponseDTO(true, "Verification code sent via WhatsApp.", null);
    }

    @PostMapping("/verifyEmail")
    public ResponseDTO verifyEmail(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = AuthUtil.getTemporaryUser(authDTO.getPrimaryKey(), authService);
        String email = (String) userData.get("userEmail");
        boolean verified = authService.verifyCode(email, authDTO.getCode());

        return AuthUtil.createVerifiedUser(verified, userData, authDTO.getPrimaryKey(), userService, authService);
    }

    @PostMapping("/verifyWhatsApp")
    public ResponseDTO verifyWhatsApp(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = AuthUtil.getTemporaryUser(authDTO.getPrimaryKey(), authService);
        String phone = (String) userData.get("phone");
        boolean verified = authService.verifyCode(phone, authDTO.getCode());

        return AuthUtil.createVerifiedUser(verified, userData, authDTO.getPrimaryKey(), userService, authService);
    }
}