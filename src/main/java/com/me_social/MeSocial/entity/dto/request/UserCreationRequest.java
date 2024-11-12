package com.me_social.MeSocial.entity.dto.request;

import java.time.Instant;

import com.me_social.MeSocial.enums.Gender;
import com.me_social.MeSocial.validator.DobConstraint;

import jakarta.validation.constraints.Size;
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
public class UserCreationRequest {
    
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;
    
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String email;
    String firstName;
    String lastName;
    String phone;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    Instant dob;

    Gender gender;

    String otp;

}
