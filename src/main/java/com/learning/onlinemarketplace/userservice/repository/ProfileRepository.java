package com.learning.onlinemarketplace.userservice.repository;

import com.learning.onlinemarketplace.userservice.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findByUserId(String userId);
}
