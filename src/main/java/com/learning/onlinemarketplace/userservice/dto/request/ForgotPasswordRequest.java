package com.learning.onlinemarketplace.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    private String email;
}
