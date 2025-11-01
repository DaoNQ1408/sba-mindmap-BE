package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.LoginRequest;
import com.sbaproject.sbamindmap.dto.request.RegisterRequest;
import com.sbaproject.sbamindmap.dto.response.LoginResponse;
import com.sbaproject.sbamindmap.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
