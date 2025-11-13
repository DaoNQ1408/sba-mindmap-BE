package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.LoginDto;
import com.sbaproject.sbamindmap.dto.request.CreateUserRequestDto;
import com.sbaproject.sbamindmap.dto.response.JWTAuthResponse;
import com.sbaproject.sbamindmap.security.JwtTokenProvider;
import com.sbaproject.sbamindmap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getMail(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody CreateUserRequestDto createUserRequestDTO) {
        userService.createAccount(createUserRequestDTO);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
