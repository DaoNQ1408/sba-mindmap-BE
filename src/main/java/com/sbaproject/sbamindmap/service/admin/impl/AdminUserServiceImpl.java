package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User create(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User request) {
        User user = getById(id);
        user.setRole(request.getRole());
        user.setUserStatus(request.getUserStatus());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) throw new RuntimeException("User not found");
        userRepository.deleteById(id);
    }
}

