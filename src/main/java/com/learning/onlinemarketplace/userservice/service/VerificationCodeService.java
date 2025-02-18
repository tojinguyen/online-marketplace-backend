package com.learning.onlinemarketplace.userservice.service;

import com.learning.onlinemarketplace.userservice.enums.VerificationType;
import com.learning.onlinemarketplace.userservice.model.VerificationCode;
import com.learning.onlinemarketplace.userservice.repository.VerificationCodeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepository verificationCodeRepository;

    private final EmailService emailService;

    public void sendVerificationCode(String email, VerificationType type) {
        // Tạo mã xác nhận
        var verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(UUID.randomUUID().toString().substring(0, 6)); // OTP with 6 digits
        verificationCode.setExpiresAt(Instant.now().plus(Duration.ofMinutes(5)));
        verificationCode.setType(type);

        log.info("Sending verification code to {} with code {}", email, verificationCode.getCode());

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
