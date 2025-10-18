package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with id: " +
                                        userId)
                );
    }
}
