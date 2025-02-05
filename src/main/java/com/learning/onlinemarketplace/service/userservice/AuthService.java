package com.learning.onlinemarketplace.service.userservice;

import com.learning.onlinemarketplace.repository.userservice.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

}
