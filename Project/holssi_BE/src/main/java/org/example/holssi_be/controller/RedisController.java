package org.example.holssi_be.controller;

import org.example.holssi_be.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
public class RedisController {
    @Autowired
    private VerificationService verificationService;

    @GetMapping
    public Map<Object, Object> getUserData(@RequestParam String email) {
        return verificationService.getTemporaryUser(email);
    }

    @DeleteMapping
    public boolean deleteUserData(@RequestParam String email) {
        return verificationService.deleteTemporaryUser(email);
    }
}
