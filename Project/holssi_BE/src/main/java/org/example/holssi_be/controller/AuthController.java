package org.example.holssi_be.controller;

import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.dto.auth.IdentifierDTO;
import org.example.holssi_be.dto.auth.AuthDTO;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.service.EmailService;
import org.example.holssi_be.service.UserService;
import org.example.holssi_be.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    /*@Autowired
    private WhatsAppService whatsAppService;*/

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseDTO user(@RequestBody UserDTO userDTO) {
        Map<String, String> userData = new HashMap<>();
        userData.put("name", userDTO.getName());
        userData.put("userEmail", userDTO.getUserEmail());
        userData.put("password", userDTO.getPassword());
        userData.put("phone", userDTO.getPhone());
        userData.put("address", userDTO.getAddress());
        userData.put("account", userDTO.getAccount());
        userData.put("bank", userDTO.getBank());

        authService.saveTemporaryUser(userDTO.getUserEmail(), userData);
        return new ResponseDTO(true, null, null);
    }

    @PostMapping("/sendEmail")
    public ResponseDTO sendEmail(@RequestBody IdentifierDTO identifierDTO) {
        String email = identifierDTO.getEmail();
        String code = authService.generateCode();
        emailService.sendEmail(email, code);
        authService.saveCode(email, code);
        return new ResponseDTO(true, "Verification code sent via email.", null);
    }

    /*@PostMapping("/sendWhatsApp")
    public String sendWhatsApp(@RequestBody IdentifierDTO identifierDTO) {
        String phone = identifierDTO.getEmail();
        String code = authService.generateCode();
        whatsAppService.sendCode(phone, code);
        authService.saveCode(phone, code);
        return new ResponseDTO(true, "Verification code sent via WhatsApp.", null);
    }*/

    @PostMapping("/verifyEmail")
    public boolean verifyEmail(@RequestBody AuthDTO authDTO) {
        String email = authDTO.getIdentifier();
        String code = authDTO.getCode();
        boolean verified = authService.verifyCode(email, code);
        if (verified) {
            Map<Object, Object> userData = authService.getTemporaryUser(email);
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
                authService.deleteTemporaryUser(email);
            }
        } else {
            authService.deleteTemporaryUser(email); // 인증 실패 시 임시 데이터 삭제
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
