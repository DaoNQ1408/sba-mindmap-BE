package com.sbaproject.sbamindmap.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    String extractUsername(String token);
}
