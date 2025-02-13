package com.learning.onlinemarketplace.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}
