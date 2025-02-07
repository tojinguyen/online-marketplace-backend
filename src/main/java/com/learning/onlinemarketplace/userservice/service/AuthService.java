package com.learning.onlinemarketplace.userservice.service;

import com.learning.onlinemarketplace.userservice.dto.LoginRequest;
import com.learning.onlinemarketplace.userservice.dto.LoginResponse;
import com.learning.onlinemarketplace.userservice.dto.RegisterRequest;
import com.learning.onlinemarketplace.userservice.model.UserAccount;
import com.learning.onlinemarketplace.userservice.repository.UserRepository;
import com.learning.onlinemarketplace.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserAccount createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

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
