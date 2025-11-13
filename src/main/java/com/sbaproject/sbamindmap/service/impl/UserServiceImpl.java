package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.CreateUserRequestDto;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.enums.UserRole;
import com.sbaproject.sbamindmap.enums.UserStatus;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with id: " +
                                        userId)
                );
    }

    @Override
    @Transactional(readOnly = false)
    public void createAccount(CreateUserRequestDto dto) {
        User user = new User();
        user.setMail(dto.getMail());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setUserStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }
}
