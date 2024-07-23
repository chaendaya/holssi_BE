package org.example.holssi_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class RedisController {

    private final AuthService authService;

    @GetMapping
    public Map<Object, Object> getUserData(@RequestParam String email) {
        return authService.getTemporaryUser(email);
    }

    @DeleteMapping
    public boolean deleteUserData(@RequestParam String email) {
        return authService.deleteTemporaryUser(email);
    }
}
