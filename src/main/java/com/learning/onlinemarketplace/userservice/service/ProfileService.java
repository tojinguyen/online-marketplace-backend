package com.learning.onlinemarketplace.userservice.service;

import com.learning.onlinemarketplace.userservice.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
}
