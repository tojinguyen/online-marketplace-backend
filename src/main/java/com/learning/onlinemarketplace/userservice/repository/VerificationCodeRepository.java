package com.learning.onlinemarketplace.userservice.repository;

import com.learning.onlinemarketplace.userservice.enums.VerificationType;
import com.learning.onlinemarketplace.userservice.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    Optional<VerificationCode> findByEmailAndType(String email, VerificationType type);
    void deleteByEmailAndType(String email, VerificationType type);

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.expiresAt < :now")
    int deleteByExpiresAtBefore(@Param("now") Instant now);
}
