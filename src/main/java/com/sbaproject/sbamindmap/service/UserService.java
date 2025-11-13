package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.CreateUserRequestDto;
import com.sbaproject.sbamindmap.entity.User;

public interface UserService {
    User findById(long userId);
    void createAccount(CreateUserRequestDto dto);
}
