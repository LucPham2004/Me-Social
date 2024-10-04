package com.me_social.MeSocial.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.response.UserDTO;
import com.me_social.MeSocial.entity.dto.response.UserResponse;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.repository.FriendShipRepository;
import com.me_social.MeSocial.repository.GroupRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {

    public UserMapper(FriendShipRepository friendShipRepository, GroupRepository groupRepository) {
        this.friendShipRepository = friendShipRepository;
        this.groupRepository = groupRepository;
    }

    FriendShipRepository friendShipRepository;
    GroupRepository groupRepository;

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

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setDob(user.getDob());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        userResponse.setGender(user.getGender());
        userResponse.setGroupNum(groupRepository.countGroupsByUserId(user.getId()));
        userResponse.setFriendNum(friendShipRepository.countAcceptedFriendshipsByUserId(user.getId()));

        return userResponse;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLocantion(user.getLocation());
        userDTO.setFriendNum(friendShipRepository.countAcceptedFriendshipsByUserId(user.getId()));

        return userDTO;
    }

}
