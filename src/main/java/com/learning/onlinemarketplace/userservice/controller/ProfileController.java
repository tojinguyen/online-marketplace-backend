package com.learning.onlinemarketplace.userservice.controller;

import com.learning.onlinemarketplace.userservice.dto.request.ProfileRequest;
import com.learning.onlinemarketplace.userservice.dto.response.ApiResponse;
import com.learning.onlinemarketplace.userservice.model.UserProfile;
import com.learning.onlinemarketplace.userservice.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> createProfile(@PathVariable String userId, @RequestBody ProfileRequest createProfileRequest) {
        try
        {
            var profile = profileService.createProfile(userId, createProfileRequest);
            return ResponseEntity.ok(ApiResponse.<UserProfile>builder()
                .success(true)
                .message("Profile created successfully.")
                .data(profile)
                .build());
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ApiResponse.<UserProfile>builder()
                .success(false)
                .message("Failed to create profile.")
                .build());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getProfileByUserId(@PathVariable String userId) {
        try
        {
            var profile = profileService.getProfileByUserId(userId);
            return ResponseEntity.ok(ApiResponse.<UserProfile>builder()
                .success(true)
                .message("Profile retrieved successfully.")
                .data(profile)
                .build());
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ApiResponse.<UserProfile>builder()
                .success(false)
                .message("Failed to retrieve profile.")
                .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(String userId, ProfileRequest updatedProfile) {
        try
        {
            var profile = profileService.updateProfile(userId, updatedProfile);
            return ResponseEntity.ok(ApiResponse.<UserProfile>builder()
                .success(true)
                .message("Profile updated successfully.")
                .data(profile)
                .build());
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ApiResponse.<UserProfile>builder()
                .success(false)
                .message("Failed to update profile.")
                .build());
        }
    }
}
