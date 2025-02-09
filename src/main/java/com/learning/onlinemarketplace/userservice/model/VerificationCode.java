package com.learning.onlinemarketplace.userservice.model;

import com.learning.onlinemarketplace.userservice.enums.VerificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "verification_code")
@Getter
@Setter
public class VerificationCode {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant expiresAt;

    @Enumerated(EnumType.STRING)
    private VerificationType type;
}
