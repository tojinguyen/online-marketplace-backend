package com.learning.onlinemarketplace.userservice.controller;

import com.learning.onlinemarketplace.userservice.dto.request.LoginRequest;
import com.learning.onlinemarketplace.userservice.dto.request.RegisterRequest;
import com.learning.onlinemarketplace.userservice.dto.request.ResetPasswordRequest;
import com.learning.onlinemarketplace.userservice.dto.request.VerifyRegisterCodeRequest;
import com.learning.onlinemarketplace.userservice.dto.response.ApiResponse;
import com.learning.onlinemarketplace.userservice.dto.response.AuthenticationResponse;
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
    public ResponseEntity<ApiResponse<String>> sendVerificationCode(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.sendRegisterVerificationCode(registerRequest.getEmail());
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Verification code sent successfully.")
                    .data("Verification code sent to " + registerRequest.getEmail())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> verify(@RequestBody VerifyRegisterCodeRequest verifyRegisterCodeRequest) {
        return ResponseEntity.ok(authService.verifyAndCreateUser(verifyRegisterCodeRequest));
    }
    //EndRegion


    //Region:  Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    //EndRegion

    //Region:  Reset Password
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody String email) {
        try {
            authService.sendResetPasswordVerificationCode(email);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Verification code sent successfully.")
                    .data("Verification code sent to " + email)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            authService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Password reset successfully.")
                    .data("Password reset successfully.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    //EndRegion

    //Region:  Logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
        try {
            authService.logout(token);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Logout successfully.")
                    .data("Logout successfully.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    //EndRegion


    //Region: Login with Google
    //EndRegion
}
