package com.learning.onlinemarketplace.repository.userservice;

import com.learning.onlinemarketplace.model.userservice.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findByUserId(String userId);
}
