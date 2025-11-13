package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {
    private String mail;
    private String password;
    private UserRole role;
}