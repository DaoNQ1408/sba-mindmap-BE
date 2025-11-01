package com.sbaproject.sbamindmap.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String mail;
    private String password;
    private String fullName;
}
