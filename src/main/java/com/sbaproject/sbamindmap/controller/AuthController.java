package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.LoginRequest;
import com.sbaproject.sbamindmap.dto.request.RegisterRequest;
import com.sbaproject.sbamindmap.dto.response.LoginResponse;
import com.sbaproject.sbamindmap.dto.response.UserResponse;
import com.sbaproject.sbamindmap.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/authentication")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
