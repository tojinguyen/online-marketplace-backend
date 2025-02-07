package com.learning.onlinemarketplace.userservice.service;

import com.learning.onlinemarketplace.userservice.dto.LoginRequest;
import com.learning.onlinemarketplace.userservice.dto.LoginResponse;
import com.learning.onlinemarketplace.userservice.dto.RegisterRequest;
import com.learning.onlinemarketplace.userservice.dto.VerifyCodeRequest;
import com.learning.onlinemarketplace.userservice.model.UserAccount;
import com.learning.onlinemarketplace.userservice.model.VerificationCode;
import com.learning.onlinemarketplace.userservice.repository.UserRepository;
import com.learning.onlinemarketplace.userservice.repository.VerificationCodeRepository;
import com.learning.onlinemarketplace.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void sendVerificationCode(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Tạo mã xác nhận
        var verificationCode = new VerificationCode();
        verificationCode.setEmail(registerRequest.getEmail());
        verificationCode.setCode(UUID.randomUUID().toString().substring(0, 6)); // OTP with 6 digits
        verificationCode.setExpiresAt(Instant.now().plus(Duration.ofMinutes(5)));

        verificationCodeRepository.deleteByEmail(registerRequest.getEmail());
        verificationCodeRepository.save(verificationCode);

        // Gửi email
        emailService.sendVerificationEmail(registerRequest.getEmail(), verificationCode.getCode());
    }

    @Transactional
    public UserAccount verifyAndCreateUser(VerifyCodeRequest registerRequest) {
        var verificationOpt = verificationCodeRepository.findByEmail(registerRequest.getEmail());
        if (verificationOpt.isEmpty() || verificationOpt.get().getExpiresAt().isBefore(Instant.now()) || !verificationOpt.get().getCode().equals(registerRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired code");
        }

        // Delete the verification code
        verificationCodeRepository.deleteByEmail(registerRequest.getEmail());

        // Create the user
        var user = new UserAccount();
        var now = Instant.now();

        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest){
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }

        var accessToken = jwtTokenProvider.generateAccessToken(user);
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }
}
