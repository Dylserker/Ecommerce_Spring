package com.judy.ecommerce.backend.controller;

import com.judy.ecommerce.backend.dto.auth.AuthDTO;
import com.judy.ecommerce.backend.dto.auth.LoginDTO;
import com.judy.ecommerce.backend.dto.auth.RegisterDTO;
import com.judy.ecommerce.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthAPIController {

    private final AuthService authService;

    public AuthAPIController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDTO> register(@RequestBody @Valid RegisterDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(@RequestBody @Valid LoginDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
