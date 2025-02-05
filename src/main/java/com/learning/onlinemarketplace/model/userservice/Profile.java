package com.learning.onlinemarketplace.model.userservice;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private String userId;

    private String name;
    private String avatar;
    private String address;
    private String phoneNumber;
    private String bio;
}
