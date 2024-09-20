package com.me_social.MeSocial.entity.dto.request;

import java.time.Instant;

import com.me_social.MeSocial.enums.Gender;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;

    private String firstName;

    private String lastName;

    private Instant dob; // date of birth

    private Gender gender;

    private String bio;

    private String location;
}
