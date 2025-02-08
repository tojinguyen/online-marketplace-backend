package com.learning.onlinemarketplace.userservice.repository;

import com.learning.onlinemarketplace.userservice.enums.VerificationType;
import com.learning.onlinemarketplace.userservice.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    Optional<VerificationCode> findByEmailAndType(String email, VerificationType type);
    void deleteByEmailAndType(String email, VerificationType type);
}
