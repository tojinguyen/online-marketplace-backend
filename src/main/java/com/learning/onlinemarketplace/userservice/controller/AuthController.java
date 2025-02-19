package com.learning.onlinemarketplace.userservice.controller;

import com.learning.onlinemarketplace.userservice.dto.request.*;
import com.learning.onlinemarketplace.userservice.dto.response.ApiResponse;
import com.learning.onlinemarketplace.userservice.dto.response.AuthenticationResponse;
import com.learning.onlinemarketplace.userservice.dto.response.ResetPasswordResponse;
import com.learning.onlinemarketplace.userservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    //Region:  Register
    @PostMapping("/send-register-code")
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
            log.info("Failed to send verification code: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .success(false)
                    .message("Failed to send verification code.")
                    .build());
        }
    }

    @PostMapping("/verify-register-code")
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
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest email) {
        try {
            authService.sendResetPasswordVerificationCode(email.getEmail());
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Verification code sent successfully.")
                    .data("Verification code sent to " + email.getEmail())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            var response = authService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.<ResetPasswordResponse>builder()
                    .success(false)
                    .message(e.getReason())  // Lấy thông báo từ ResponseStatusException
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<ResetPasswordResponse>builder()
                    .success(false)
                    .message("An unexpected error occurred")
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
