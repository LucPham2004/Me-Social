package com.me_social.MeSocial.entity.dto.response;

import com.me_social.MeSocial.enums.Gender;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class UserCreationResponse {
     String username;

     String password;
     String email;
     String firstName;
     String lastName;
     String phone;
     Instant dob;

     Instant createdAt;
     Instant updatedAt;
     
     Gender gender;
}
