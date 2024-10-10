package com.me_social.MeSocial.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.request.UserUpdateRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.UserDTO;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.UserMapper;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    GroupRepository groupRepository;
    PasswordEncoder passwordEncoder;

    static int USERS_PER_PAGE = 20;

    // GET

    // Get Group members
    public Page<User> getGroupMembers(Long groupId, int pageNum) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> members = groupRepository.findMembersById(groupId, pageable);

        return members;
    }

    // Get Group admins
    public Page<User> getGroupAdmins(Long groupId, int pageNum) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> admins = groupRepository.findAdminsById(groupId, pageable);

        return admins;
    }

    // Get User friends
    public Page<User> getUserFriends(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> friends = userRepository.findFriends(userId, pageable);

        return friends;
    }

    // Get mutual friends
    public ApiResponse<Page<UserDTO>> getMutualFriends(Long meId, Long youId, int pageNum) {
        if (!userRepository.existsById(meId) || !userRepository.existsById(youId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> mutualFriends = userRepository.findMutualFriends(meId, youId, pageable);

        ApiResponse<Page<UserDTO>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get mutual friends successfully");
        apiResponse.setResult(getUsersWithMutualFriendsCount(meId, mutualFriends));

        return apiResponse;
    }

    // USER CRUD

    public User handleGetUserByUsernameOrEmailOrPhone(String loginInput) {
        Optional<User> optionalUser = this.userRepository.findByEmail(loginInput);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByUsername(loginInput);
        }
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByPhone(loginInput);
        }
        if (optionalUser.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return optionalUser.get();
    }

    public Optional<User> findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        return optionalUser;
    }

    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())
                || userRepository.existsByEmail(request.getEmail())
                || userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public User getUser(Long id) {
        User dbUser = userRepository.findById(id).get();

        return dbUser;
    }

    public User updateUser(UserUpdateRequest reqUser) {
        var dbUser = this.findById(reqUser.getId()).get();

        dbUser.setFirstName(reqUser.getFirstName());
        dbUser.setLastName(reqUser.getLastName());
        dbUser.setGender(reqUser.getGender());
        dbUser.setBio(reqUser.getBio());
        dbUser.setDob(reqUser.getDob());
        dbUser.setLocation(reqUser.getLocation());

        return this.userRepository.save(dbUser);
    }

    public void deleteUserById(Long id) {
        User dbUser = this.findById(id).get();

        userRepository.delete(dbUser);
    }

    public Page<User> getAllUsers(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<User> users = userRepository.findAll(pageable);

        return users;
    }

    // Other methods

    // Map to DTO with mutual friends count for many users
    private Page<UserDTO> getUsersWithMutualFriendsCount(Long userId, Page<User> users) {
        List<Long> userIds = users.getContent().stream().map(User::getId).collect(Collectors.toList());
        Map<Long, Long> mutualFriendsCount = userRepository.countMutualFriendsForUsers(userId, userIds);

        return users.map(user -> {
            UserDTO dto = userMapper.toUserDTO(user);
            dto.setMutualFriendsNum(mutualFriendsCount.getOrDefault(user.getId(), 0L));
            return dto;
        });
    }

}
