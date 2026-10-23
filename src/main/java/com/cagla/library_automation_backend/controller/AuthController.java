package com.cagla.library_automation_backend.controller;

import com.cagla.library_automation_backend.entity.UserAccount;
import com.cagla.library_automation_backend.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        Map<String, Object> response = new HashMap<>();

        boolean success = authService.login(username, password);

        response.put("success", success);

        if (success) {
            response.put("message", "Giriş başarılı");
        } else {
            response.put("message", "Kullanıcı adı veya şifre hatalı");
        }

        return response;
    }

    @PostMapping("/register")
    public UserAccount register(@RequestBody UserAccount userAccount) {
        return authService.register(userAccount);
    }
}