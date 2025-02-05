package com.learning.onlinemarketplace.repository.userservice;

import com.learning.onlinemarketplace.model.userservice.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
