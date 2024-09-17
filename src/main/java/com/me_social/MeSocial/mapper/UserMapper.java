package com.me_social.MeSocial.mapper;

import org.mapstruct.Mapper;
import java.time.Instant;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.response.UserCreationResponse;
import com.me_social.MeSocial.entity.dto.response.UserResponse;
import com.me_social.MeSocial.entity.modal.User;

@Mapper(componentModel = "spring")
public class UserMapper {

    public User toUser(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setCreatedAt(Instant.now());
        user.setGender(request.getGender());

        return user;
    };

    public UserCreationResponse toUserCreationResponse(UserCreationRequest request) {
        UserCreationResponse user = new UserCreationResponse();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setCreatedAt(Instant.now());
        user.setGender(request.getGender());

        return user;
    }

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setDob(user.getDob());
        userResponse.setCreatedAt(Instant.now());
        userResponse.setGender(user.getGender());

        return userResponse;
    }

}
