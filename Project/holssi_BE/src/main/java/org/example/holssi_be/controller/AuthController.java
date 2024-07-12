package org.example.holssi_be.controller;

import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.dto.request.EmailRequestDTO;
import org.example.holssi_be.dto.request.VerifyEmailDTO;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.service.EmailService;
import org.example.holssi_be.service.UserService;
import org.example.holssi_be.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private EmailService emailService;

    /*@Autowired
    private WhatsAppService whatsAppService;*/

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        Map<String, String> userData = new HashMap<>();
        userData.put("name", userDTO.getName());
        userData.put("userEmail", userDTO.getUserEmail());
        userData.put("password", userDTO.getPassword());
        userData.put("phone", userDTO.getPhone());
        userData.put("address", userDTO.getAddress());
        userData.put("account", userDTO.getAccount());
        userData.put("bank", userDTO.getBank());

        verificationService.saveTemporaryUser(userDTO.getUserEmail(), userData);
        return "Choose verification method: email or whatsapp";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        String email = emailRequestDTO.getEmail();
        String code = verificationService.generateVerificationCode();
        emailService.sendVerificationEmail(email, code);
        verificationService.saveVerificationCode(email, code);
        return "Verification code sent via email.";
    }

    /*@PostMapping("/sendWhatsApp")
    public String sendWhatsApp(@RequestParam String phone) {
        String code = verificationService.generateVerificationCode();
        whatsAppService.sendVerificationCode(phone, code);
        verificationService.saveVerificationCode(phone, code);
        return "Verification code sent via WhatsApp.";
    }*/

    @PostMapping("/verifyEmail")
    public boolean verifyEmail(@RequestBody VerifyEmailDTO verifyEmailDTO) {
        String email = verifyEmailDTO.getEmail();
        String code = verifyEmailDTO.getCode();
        boolean verified = verificationService.verifyCode(email, code);
        if (verified) {
            Map<Object, Object> userData = verificationService.getTemporaryUser(email);
            if (!userData.isEmpty()) {
                Users user = new Users();
                user.setName((String) userData.get("name"));
                user.setUserEmail((String) userData.get("userEmail"));
                user.setPassword((String) userData.get("password"));
                user.setPhone((String) userData.get("phone"));
                user.setAddress((String) userData.get("address"));
                user.setAccount((String) userData.get("account"));
                user.setBank((String) userData.get("bank"));
                user.setVerified(true);
                userService.save(user);
                verificationService.deleteTemporaryUser(email);
            }
        } else {
            verificationService.deleteTemporaryUser(email); // 인증 실패 시 임시 데이터 삭제
        }
        return verified;
    }

    /*@PostMapping("/verifyWhatsApp")
    public boolean verifyWhatsApp(@RequestParam String phoneNumber, @RequestParam String code) {
        boolean verified = verificationService.verifyCode(phoneNumber, code);
        if (verified) {
            Map<Object, Object> userData = verificationService.getTemporaryUser(phoneNumber);
            if (!userData.isEmpty()) {
                User user = new User();
                user.setName((String) userData.get("name"));
                user.setEmail((String) userData.get("email"));
                user.setPhoneNumber((String) userData.get("phoneNumber"));
                user.setAddress((String) userData.get("address"));
                user.setAccountNumber((String) userData.get("accountNumber"));
                user.setVerified(true);
                userService.save(user);
                verificationService.deleteTemporaryUser(phoneNumber);
            }
        } else {
            verificationService.deleteTemporaryUser(phoneNumber); // 인증 실패 시 임시 데이터 삭제
        }
        return verified;
    }

    */
}
