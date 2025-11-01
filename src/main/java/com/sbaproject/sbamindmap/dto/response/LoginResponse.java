package com.sbaproject.sbamindmap.dto.response;

import com.sbaproject.sbamindmap.enums.UserRole;
import com.sbaproject.sbamindmap.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String email;
    private String fullName;
    private UserRole role;
    private UserStatus status;
    private String token;
}
