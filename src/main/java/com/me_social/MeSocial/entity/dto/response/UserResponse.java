package com.me_social.MeSocial.entity.dto.response;

import java.time.Instant;

import com.me_social.MeSocial.enums.Gender;

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
public class UserResponse {
     Long id;

     String username;
     String email;
     String firstName;
     String lastName;
     String phone;
     String avatarUrl;

     Instant dob;
     Instant createdAt;
     Instant updatedAt;
     
     Gender gender;

     int groupNum;
     int friendNum;
     Long mutualFriendsNum;
}
