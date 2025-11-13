package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.enums.UserStatus;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepository.getByMail(mail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + mail);
        }
        if (user.getUserStatus() == UserStatus.BANNED) {
            throw new LockedException("Your account has been banned.");
        }
        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new DisabledException("Your account is inactive.");
        }
        return new CustomUserDetails(user);
    }
}
