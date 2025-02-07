package com.learning.onlinemarketplace.userservice.controller;

import com.learning.onlinemarketplace.userservice.dto.LoginRequest;
import com.learning.onlinemarketplace.userservice.dto.LoginResponse;
import com.learning.onlinemarketplace.userservice.dto.RegisterRequest;
import com.learning.onlinemarketplace.userservice.model.UserAccount;
import com.learning.onlinemarketplace.userservice.repository.UserRepository;
import com.learning.onlinemarketplace.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserAccount> register(@RequestBody RegisterRequest registerRequest) {
        var userAccount = authService.createUser(registerRequest);
        return new ResponseEntity<>(userAccount, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
