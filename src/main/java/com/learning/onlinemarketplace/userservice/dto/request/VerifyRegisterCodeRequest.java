package com.learning.onlinemarketplace.userservice.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRegisterCodeRequest {
    private String email;
    private String password;
    private String code;
}
