package com.learning.onlinemarketplace.userservice.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @PostMapping("/create")
    public void createProfile() {
        // create profile
    }

    @PutMapping("/update")
    public void updateProfile() {
        // update profile
    }
}
