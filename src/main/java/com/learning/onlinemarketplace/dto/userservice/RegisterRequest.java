package com.learning.onlinemarketplace.dto.userservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String role;
}
