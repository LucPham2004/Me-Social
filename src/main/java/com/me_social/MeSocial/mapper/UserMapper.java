package com.me_social.MeSocial.mapper;

import org.mapstruct.Mapper;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.modal.User;

@Mapper(componentModel = "spring")
public class UserMapper {

    public User toUser(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setGender(request.getGender());

        return user;
    };

}
