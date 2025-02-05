package com.learning.onlinemarketplace.service.userservice;

import com.learning.onlinemarketplace.repository.userservice.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
}
