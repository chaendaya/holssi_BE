package org.example.holssi_be.controller;

import org.example.holssi_be.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
public class RedisController {
    @Autowired
    private AuthService authService;

    @GetMapping
    public Map<Object, Object> getUserData(@RequestParam String email) {
        return authService.getTemporaryUser(email);
    }

    @DeleteMapping
    public boolean deleteUserData(@RequestParam String email) {
        return authService.deleteTemporaryUser(email);
    }
}
