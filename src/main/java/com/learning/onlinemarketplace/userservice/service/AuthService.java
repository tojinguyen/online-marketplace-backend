package com.learning.onlinemarketplace.userservice.service;

import com.learning.onlinemarketplace.redisservice.service.BaseRedisService;
import com.learning.onlinemarketplace.userservice.dto.request.LoginRequest;
import com.learning.onlinemarketplace.userservice.dto.request.ResetPasswordRequest;
import com.learning.onlinemarketplace.userservice.dto.request.VerifyRegisterCodeRequest;
import com.learning.onlinemarketplace.userservice.dto.response.ApiResponse;
import com.learning.onlinemarketplace.userservice.dto.response.AuthenticationResponse;
import com.learning.onlinemarketplace.userservice.dto.response.ResetPasswordResponse;
import com.learning.onlinemarketplace.userservice.enums.VerificationType;
import com.learning.onlinemarketplace.userservice.model.UserAccount;
import com.learning.onlinemarketplace.userservice.repository.AccountRepository;
import com.learning.onlinemarketplace.userservice.repository.VerificationCodeRepository;
import com.learning.onlinemarketplace.userservice.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final AccountRepository userRepository;

    private final VerificationCodeRepository verificationCodeRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtTokenProvider;

    private final VerificationCodeService verificationCodeService;

    private final BaseRedisService baseRedisService;

    private ProfileService profileService;


    // Region: Register
    @Transactional
    public void sendRegisterVerificationCode(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        verificationCodeService.sendVerificationCode(email, VerificationType.REGISTER);
    }

    @Transactional
    public ApiResponse<AuthenticationResponse> verifyAndCreateUser(VerifyRegisterCodeRequest registerRequest) {
        var verificationOpt = verificationCodeRepository.findByEmailAndType(registerRequest.getEmail(), VerificationType.REGISTER);
        if (verificationOpt.isEmpty() || verificationOpt.get().getExpiresAt().isBefore(Instant.now()) || !verificationOpt.get().getCode().equals(registerRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired code");
        }

        // Delete the verification code
        verificationCodeRepository.deleteByEmailAndType(registerRequest.getEmail(), VerificationType.REGISTER);

        // Create the user
        var user = new UserAccount();
        var now = Instant.now();

        user.setUserId(UUID.randomUUID().toString());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        userRepository.save(user);

        var accessToken = jwtTokenProvider.generateAccessToken(user);
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);
        var authResponse = new AuthenticationResponse(accessToken, refreshToken);

        return ApiResponse.<AuthenticationResponse>builder().success(true).message("User created successfully").data(authResponse).build();
    }
    // EndRegion

    // Region: Reset Password
    @Transactional
    public void sendResetPasswordVerificationCode(String email) {
        verificationCodeService.sendVerificationCode(email, VerificationType.RESET_PASSWORD);
    }

    @Transactional
    public ApiResponse<ResetPasswordResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        var verificationOpt = verificationCodeRepository.findByEmailAndType(resetPasswordRequest.getEmail(), VerificationType.RESET_PASSWORD);
        if (verificationOpt.isEmpty() || verificationOpt.get().getExpiresAt().isBefore(Instant.now()) || !verificationOpt.get().getCode().equals(resetPasswordRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired code");
        }

        // Delete the verification code
        verificationCodeRepository.deleteByEmailAndType(resetPasswordRequest.getEmail(), VerificationType.RESET_PASSWORD);

        // Update the user's password
        var user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found"));

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);

        return ApiResponse.<ResetPasswordResponse>builder().success(true)
                .message("Password reset successfully")
                .data(new ResetPasswordResponse(true))
                .build();
    }
    // EndRegion

    // Region: Login
    public ApiResponse<AuthenticationResponse> login(LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }

        var accessToken = jwtTokenProvider.generateAccessToken(user);
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);
        var authResponse = new AuthenticationResponse(accessToken, refreshToken);

        return ApiResponse.<AuthenticationResponse>builder().success(true).message("Login Success").data(authResponse).build();
    }
    // EndRegion

    // Region: Logout
    public void logout(String token) {
        // Remove prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

        var expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);
        long ttl = Date.from(expirationDate).getTime() - System.currentTimeMillis();
        if (ttl <= 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token has expired.");
        }

        baseRedisService.set("blacklist:" + token, "blacklisted", ttl / 1000);
        log.info("Token {} has been blacklisted", token);
    }
    // EndRegion

    // Region: Google Login
    public void ProcessOAuthPostLogin(String email) {
        Optional<UserAccount> existUser = userRepository.findByEmail(email);
        if (existUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        } else {
            var newUserAccount = new UserAccount();
            var now = Instant.now();

            newUserAccount.setEmail(email);
            newUserAccount.setCreatedAt(now);
            newUserAccount.setUpdatedAt(now);

            userRepository.save(newUserAccount);
        }
    }
    // EndRegion
}
