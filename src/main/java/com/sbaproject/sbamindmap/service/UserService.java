package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(long userId);
}
