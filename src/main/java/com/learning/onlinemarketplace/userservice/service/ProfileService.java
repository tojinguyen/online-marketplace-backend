package com.learning.onlinemarketplace.userservice.service;

import com.learning.onlinemarketplace.userservice.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
}
