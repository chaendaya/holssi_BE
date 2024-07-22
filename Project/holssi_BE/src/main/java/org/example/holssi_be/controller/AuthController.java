package org.example.holssi_be.controller;

import jakarta.validation.Valid;
import org.example.holssi_be.dto.*;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.service.*;
import org.example.holssi_be.util.AuthUtil;
import org.example.holssi_be.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private CollectorService collectorService;

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/user")
    public ResponseDTO registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        Map<String, String> userData = AuthUtil.createTemporaryUser(userDTO);
        authService.saveTemporaryUser(userDTO.getUserEmail(), userData);
        return new ResponseDTO(true, null, null);
    }

    @PostMapping("/collector")
    public ResponseDTO registerCollector(@RequestBody @Valid CollectorDTO collectorDTO, BindingResult bindingResult){
        ValidationUtil.validateRequest(bindingResult);

        Map<String, String> collectorData = AuthUtil.createTemporaryCollector(collectorDTO);
        authService.saveTemporaryCollector(collectorDTO.getCollectorEmail(), collectorData);
        return new ResponseDTO(true, null, null);
    }

    @PostMapping("/sendEmail")
    public ResponseDTO sendEmail(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        String email = AuthUtil.getEmailByRole(authDTO, authService);
        String code = authService.generateCode();

        emailService.sendEmail(email, code);
        authService.saveCode(email, code);
        return new ResponseDTO(true, "Verification code sent via email.", null);
    }

    @PostMapping("/sendWhatsApp")
    public ResponseDTO sendWhatsApp(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        String phone = AuthUtil.getPhoneByRole(authDTO, authService);
        String code = authService.generateCode();

        whatsAppService.sendWhatsApp(phone, code);
        authService.saveCode(phone, code);
        return new ResponseDTO(true, "Verification code sent via WhatsApp.", null);
    }

    @PostMapping("/verifyEmail")
    public ResponseDTO verifyEmail(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        String email = AuthUtil.getEmailByRole(authDTO, authService);
        boolean verified = authService.verifyCode(email, authDTO.getCode());

        return AuthUtil.verificationResult(authDTO, verified, userService, collectorService, authService, memberRepository);
    }

    @PostMapping("/verifyWhatsApp")
    public ResponseDTO verifyWhatsApp(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        ValidationUtil.validateRequest(bindingResult);

        String phone = AuthUtil.getPhoneByRole(authDTO, authService);
        boolean verified = authService.verifyCode(phone, authDTO.getCode());

        return AuthUtil.verificationResult(authDTO, verified, userService, collectorService, authService, memberRepository);
    }
}