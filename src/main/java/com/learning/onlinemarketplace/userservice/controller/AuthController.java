package com.learning.onlinemarketplace.userservice.controller;

import com.learning.onlinemarketplace.userservice.dto.*;
import com.learning.onlinemarketplace.userservice.model.UserAccount;
import com.learning.onlinemarketplace.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    //Region:  Register
    @PostMapping("/send-verification-code")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> sendVerificationCode(@RequestBody RegisterRequest registerRequest) {
        authService.sendRegisterVerificationCode(registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Verification code sent to " + registerRequest.getEmail());
    }

    @PostMapping("/verify")
    public ResponseEntity<UserAccount> verify(@RequestBody VerifyRegisterCodeRequest verifyRegisterCodeRequest) {
        return ResponseEntity.ok(authService.verifyAndCreateUser(verifyRegisterCodeRequest));
    }
    //EndRegion


    //Region:  Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    //EndRegion

    //Region:  Reset Password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        authService.sendResetPasswordVerificationCode(email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Verification code sent to " + email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password reset successfully");
    }

    //EndRegion
}
