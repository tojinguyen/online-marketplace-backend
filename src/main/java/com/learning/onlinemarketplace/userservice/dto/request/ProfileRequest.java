package com.learning.onlinemarketplace.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String name;
    private String avatarUrl;
    private String address;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
}
