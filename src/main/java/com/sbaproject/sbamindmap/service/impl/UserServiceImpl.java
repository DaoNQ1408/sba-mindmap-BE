package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.CreateUserRequestDto;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.entity.Wallet;
import com.sbaproject.sbamindmap.enums.UserStatus;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.repository.WalletRepository;
import com.sbaproject.sbamindmap.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
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
        // Tạo user
        User user = new User();
        user.setMail(dto.getMail());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setUserStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        // Tự động tạo wallet cho user mới
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(0.0);
        wallet.setCreatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
    }
}
