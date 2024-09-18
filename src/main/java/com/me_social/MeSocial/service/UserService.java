package com.me_social.MeSocial.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.UserResponse;
import com.me_social.MeSocial.entity.modal.Follow;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.UserMapper;
import com.me_social.MeSocial.repository.FollowRepository;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.UserRepository;
import com.me_social.MeSocial.utils.PaginationUtil;

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
    FollowRepository followRepository;
    PasswordEncoder passwordEncoder;

    static int USERS_PER_PAGE = 10;

    // GET

    // Get Group members
    public ApiResponse<Page<User>> getGroupMembers(Long groupId, int pageNum) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> members = PaginationUtil.convertSetToPage(groupRepository.findById(groupId).getMembers(), pageable);

        ApiResponse<Page<User>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Info successfully");
        apiResponse.setResult(members);

        return apiResponse;
    }

    // Get Group admins
    public ApiResponse<Set<User>> getGroupAdmins(Long groupId, int pageNum) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Set<User> admins = groupRepository.findById(groupId).getAdmins();

        ApiResponse<Set<User>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Info successfully");
        apiResponse.setResult(admins);

        return apiResponse;
    }

    // Get User's followers
    public ApiResponse<Page<User>> getFollowers(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        // important
        Page<User> followers = followRepository.findByFollower(userRepository.findById(userId), pageable)
                .map(Follow::getFollower);

        ApiResponse<Page<User>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get followers successfully");
        apiResponse.setResult(followers);

        return apiResponse;
    }

    // Get User's followers
    public ApiResponse<Page<User>> getFollowings(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        // important
        Page<User> followings = followRepository.findByFollowing(userRepository.findById(userId), pageable)
                .map(Follow::getFollowing);

        ApiResponse<Page<User>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get followers successfully");
        apiResponse.setResult(followings);

        return apiResponse;
    }

    // USER CRUD

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public ApiResponse<UserResponse> createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())
                || userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toUser(request);

        userRepository.save(user);
        UserResponse userResponse = userMapper.toUserResponse(user);

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Create user successfully");
        apiResponse.setResult(userResponse);

        return apiResponse;
    }

    public ApiResponse<UserResponse> getUser(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        UserResponse userResponse = userMapper.toUserResponse(user);
        
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get user by id successfully");
        apiResponse.setResult(userResponse);

        return apiResponse;
    }
}
