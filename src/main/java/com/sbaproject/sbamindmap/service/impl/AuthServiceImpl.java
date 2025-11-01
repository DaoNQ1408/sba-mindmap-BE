package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.LoginRequest;
import com.sbaproject.sbamindmap.dto.request.RegisterRequest;
import com.sbaproject.sbamindmap.dto.response.LoginResponse;
import com.sbaproject.sbamindmap.dto.response.UserResponse;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.enums.UserRole;
import com.sbaproject.sbamindmap.enums.UserStatus;
import com.sbaproject.sbamindmap.exception.DuplicateResourceException;
import com.sbaproject.sbamindmap.mapper.UserMapper;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.service.AuthService;
import com.sbaproject.sbamindmap.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername((registerRequest.getUsername()))) {
            throw new DuplicateResourceException("Tên đăng nhập đã tồn tại");
        }

        if (userRepository.existsByMail(registerRequest.getMail())) {
            throw new DuplicateResourceException("Email đã được sử dụng");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setMail(registerRequest.getMail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setRole(UserRole.USER);
        user.setUserStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        if (loginRequest.getMail() == null || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Thiếu thông tin đăng nhập");
        }

        User user = userRepository.findByMail(loginRequest.getMail())
                .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập không tồn tại"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        String accessToken = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse(user.getMail(),
                                                    user.getFullName(),
                                                    user.getRole(),
                                                    user.getUserStatus(),
                                                    accessToken);
        userRepository.save(user);

        return response;
    }
}
