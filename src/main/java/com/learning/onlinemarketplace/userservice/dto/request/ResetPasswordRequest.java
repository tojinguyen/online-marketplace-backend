package com.learning.onlinemarketplace.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String email;
    private String password;
    private String code;
}
