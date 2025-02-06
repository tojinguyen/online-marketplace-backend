package com.learning.onlinemarketplace.repository.userservice;

import com.learning.onlinemarketplace.model.userservice.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
