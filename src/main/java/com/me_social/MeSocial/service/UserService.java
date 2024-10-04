package com.me_social.MeSocial.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
import com.me_social.MeSocial.entity.dto.response.UserResponse;
import com.me_social.MeSocial.entity.modal.Friendship;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.enums.FriendshipStatus;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.UserMapper;
import com.me_social.MeSocial.repository.FriendShipRepository;
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
    FriendShipRepository friendShipRepository;
    PasswordEncoder passwordEncoder;

    static int USERS_PER_PAGE = 20;

    // GET

    // Get Group members
    public ApiResponse<Page<UserDTO>> getGroupMembers(Long groupId, int pageNum) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> members = groupRepository.findMembersById(groupId, pageable);

        ApiResponse<Page<UserDTO>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Info successfully");
        apiResponse.setResult(members.map(userMapper::toUserDTO));

        return apiResponse;
    }

    // Get Group admins
    public ApiResponse<Page<UserDTO>> getGroupAdmins(Long groupId, int pageNum) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> admins = groupRepository.findAdminsById(groupId, pageable);

        ApiResponse<Page<UserDTO>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Info successfully");
        apiResponse.setResult(admins.map(userMapper::toUserDTO));

        return apiResponse;
    }

    // Get User friends
    public ApiResponse<Page<UserDTO>> getUserFriends(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        // important
        Page<Friendship> friendshipsRequested = friendShipRepository.findByRequesterIdOrRequestReceiverId(userId, pageable);

        Set<User> acceptedFriends = new HashSet<>();
        
        for(Friendship friendship: friendshipsRequested) {
            if(friendship.getStatus().equals(FriendshipStatus.ACCEPTED)) {
                acceptedFriends.add(friendship.getRequester());
            }
        }

        ApiResponse<Page<UserDTO>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get followers successfully");
        apiResponse.setResult(PaginationUtil.convertSetToPage(acceptedFriends.stream()
                                .map(userMapper::toUserDTO)
                                .collect(Collectors.toSet()), pageable));

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

    public User handleGetUserById(Long id) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(id));
        if (optionalUser == null) {
            return null;
        }
        return optionalUser.get();
    }

    public ApiResponse<UserResponse> createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())
                || userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toUser(request);
        user.setCreatedAt(Instant.now());
        userRepository.save(user);

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Create user successfully");
        apiResponse.setResult(userMapper.toUserResponse(user));

        return apiResponse;
    }

    public ApiResponse<UserResponse> getUser(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get user by id successfully");
        apiResponse.setResult(userMapper.toUserResponse(user));

        return apiResponse;
    }

    public ApiResponse<UserResponse> updateUser(UserUpdateRequest reqUser) {
        User dbUser = handleGetUserById(reqUser.getId());
        if (dbUser == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        dbUser.setFirstName(reqUser.getFirstName());
        dbUser.setLastName(reqUser.getLastName());
        dbUser.setGender(reqUser.getGender());
        dbUser.setBio(reqUser.getBio());
        dbUser.setDob(reqUser.getDob());
        dbUser.setLocation(reqUser.getLocation());
        dbUser.setUpdatedAt(Instant.now());
        userRepository.save(dbUser);

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(10345300);
        apiResponse.setMessage("Update User Successfully!");
        apiResponse.setResult(userMapper.toUserResponse(dbUser));

        return apiResponse;
    }

}
