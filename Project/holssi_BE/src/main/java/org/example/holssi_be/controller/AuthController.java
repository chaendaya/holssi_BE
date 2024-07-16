package org.example.holssi_be.controller;

import jakarta.validation.Valid;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.dto.AuthDTO;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.service.AuthService;
import org.example.holssi_be.service.EmailService;
import org.example.holssi_be.service.UserService;
import org.example.holssi_be.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseDTO user(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseDTO(false, null, bindingResult.getFieldError().getDefaultMessage());
        }

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
    public ResponseDTO sendEmail(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseDTO(false, null, bindingResult.getFieldError().getDefaultMessage());
        }

        String key = authDTO.getPrimaryKey();
        String email = authDTO.getIdentifier();

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = authService.getTemporaryUser(key);

        if (userData.isEmpty()) {
            return new ResponseDTO(false, null, "Temporary user data not found.");
        }

        // 요청 바디의 identifier와 임시 사용자 데이터의 email 비교
        String storedEmail = (String) userData.get("userEmail");
        if (!email.equals(storedEmail)) {
            return new ResponseDTO(false, null, "Email does not match.");
        }

        String code = authService.generateCode();
        emailService.sendEmail(email, code);
        authService.saveCode(email, code);
        return new ResponseDTO(true, "Verification code sent via email.", null);
    }

    @PostMapping("/sendWhatsApp")
    public ResponseDTO sendWhatsApp(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseDTO(false, null, bindingResult.getFieldError().getDefaultMessage());
        }

        String key = authDTO.getPrimaryKey();
        String phone = authDTO.getIdentifier();

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = authService.getTemporaryUser(key);

        if (userData.isEmpty()) {
            return new ResponseDTO(false, null, "Temporary user data not found.");
        }

        // 요청 바디의 identifier와 임시 사용자 데이터의 phone 비교
        String storedPhone = (String) userData.get("phone");
        if (!phone.equals(storedPhone)) {
            return new ResponseDTO(false, null, "Phone number does not match.");
        }

        String code = authService.generateCode();
        whatsAppService.sendWhatsApp(phone, code);
        authService.saveCode(phone, code);
        return new ResponseDTO(true, "Verification code sent via WhatsApp.", null);
    }

    @PostMapping("/verifyEmail")
    public ResponseDTO verifyEmail(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseDTO(false, null, bindingResult.getFieldError().getDefaultMessage());
        }

        String key = authDTO.getPrimaryKey();
        String email = authDTO.getIdentifier();

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = authService.getTemporaryUser(key);

        if (userData.isEmpty()) {
            return new ResponseDTO(false, null, "Temporary user data not found.");
        }

        // 요청 바디의 identifier와 임시 사용자 데이터의 email 비교
        String storedEmail = (String) userData.get("userEmail");
        if (!email.equals(storedEmail)) {
            return new ResponseDTO(false, null, "Email does not match.");
        }

        String code = authDTO.getCode();
        boolean verified = authService.verifyCode(email, code);
        if (verified && !userData.isEmpty()) {
            Users user = new Users();
            user.setName((String) userData.get("name"));
            user.setUserEmail((String) userData.get("userEmail"));
            user.setPassword((String) userData.get("password"));
            user.setPhone((String) userData.get("phone"));
            user.setAddress((String) userData.get("address"));
            user.setAccount((String) userData.get("account"));
            user.setBank((String) userData.get("bank"));
            userService.save(user);
            authService.deleteTemporaryUser(key);
        } else {
            authService.deleteTemporaryUser(key); // 인증 실패 시 임시 데이터 삭제
            return new ResponseDTO(false, null, "Verification Code does not match.");
        }
        return new ResponseDTO(true, "Verification Completed.", null);
    }

    @PostMapping("/verifyWhatsApp")
    public ResponseDTO verifyWhatsApp(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseDTO(false, null, bindingResult.getFieldError().getDefaultMessage());
        }

        String key = authDTO.getPrimaryKey();
        String phone = authDTO.getIdentifier();

        // 임시 사용자 데이터 가져오기
        Map<Object, Object> userData = authService.getTemporaryUser(key);

        if (userData.isEmpty()) {
            return new ResponseDTO(false, null, "Temporary user data not found.");
        }

        // 요청 바디의 identifier와 임시 사용자 데이터의 phone 비교
        String storedPhone = (String) userData.get("phone");
        if (!phone.equals(storedPhone)) {
            return new ResponseDTO(false, null, "Phone number does not match.");
        }

        String code = authDTO.getCode();
        boolean verified = authService.verifyCode(phone, code);
        if (verified && !userData.isEmpty()) {
            Users user = new Users();
            user.setName((String) userData.get("name"));
            user.setUserEmail((String) userData.get("userEmail"));
            user.setPassword((String) userData.get("password"));
            user.setPhone((String) userData.get("phone"));
            user.setAddress((String) userData.get("address"));
            user.setAccount((String) userData.get("account"));
            user.setBank((String) userData.get("bank"));
            userService.save(user);
            authService.deleteTemporaryUser(key);
        } else {
            authService.deleteTemporaryUser(key); // 인증 실패 시 임시 데이터 삭제
            return new ResponseDTO(false, null, "Verification Code does not match.");
        }
        return new ResponseDTO(true, "Verification Completed.", null);
    }
}
