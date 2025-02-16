package com.learning.onlinemarketplace.userservice.service;

import com.learning.onlinemarketplace.userservice.enums.VerificationType;
import com.learning.onlinemarketplace.userservice.model.VerificationCode;
import com.learning.onlinemarketplace.userservice.repository.UserRepository;
import com.learning.onlinemarketplace.userservice.repository.VerificationCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class VerificationCodeService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void sendVerificationCode(String email, VerificationType type) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Tạo mã xác nhận
        var verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(UUID.randomUUID().toString().substring(0, 6)); // OTP with 6 digits
        verificationCode.setExpiresAt(Instant.now().plus(Duration.ofMinutes(5)));
        verificationCode.setType(type);

        log.info("Sending verification code to " + email + " with code " + verificationCode.getCode());

        verificationCodeRepository.deleteByEmailAndType(email, type);
        verificationCodeRepository.save(verificationCode);

        // Gửi email
        emailService.sendVerificationEmail(email, verificationCode.getCode());
    }

    @Scheduled(cron = "0 0 1 * * ?") // Run at 01:00 AM every day
    public void deleteExpiredVerificationCodes() {
        log.info("Scheduled job is running...");
        var deletedCount = verificationCodeRepository.deleteByExpiresAtBefore(Instant.now());
        log.info("Deleted {} expired verification codes", deletedCount);
    }
}
