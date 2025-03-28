package com.me_social.MeSocial.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.request.UserUpdateRequest;
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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    static int USERS_PER_PAGE = 20;
    UserRepository userRepository;
    UserMapper userMapper;
    GroupRepository groupRepository;
    PasswordEncoder passwordEncoder;

    // GET

    // Get Group members
    public Page<User> getGroupMembers(Long groupId, Pageable pageable) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return groupRepository.findMembersById(groupId, pageable);
    }

    // Get Group admins
    public Page<User> getGroupAdmins(Long groupId, Pageable pageable) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return groupRepository.findAdminsById(groupId, pageable);
    }

    // Get User friends
    public Page<UserDTO> getUserFriends(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Page<User> friends = userRepository.findFriends(userId, pageable);

        return getUsersWithMutualFriendsCount(userId, friends);
    }

    // Get User friends
    public Page<UserDTO> getSuggestedFriends(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Page<User> friends = userRepository.findSuggestedFriends(userId, pageable);

        return getUsersWithMutualFriendsCount(userId, friends);
    }

    // Get mutual friends
    public Page<UserDTO> getMutualFriends(Long meId, Long youId, Pageable pageable) {
        if (!userRepository.existsById(meId) || !userRepository.existsById(youId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Page<User> mutualFriends = userRepository.findMutualFriends(meId, youId, pageable);

        return getUsersWithMutualFriendsCount(meId, mutualFriends);
    }

    // Get User by Id
    public User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        } else {
            return optionalUser.get();
        }
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    // Get all Users
    public Page<User> getAllUsers(Specification<User> specification, Pageable pageable) {
//        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return userRepository.findAll(specification, pageable);
    }

    // Get User by username/email/phone
    public User handleGetUserByUsernameOrEmailOrPhone(String loginInput) {
        Optional<User> optionalUser = this.userRepository.findByEmail(loginInput);
        log.info("login input: {}", loginInput);
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

    // POST
    // Create user
    public User createUser(UserCreationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())
                || userRepository.existsByEmail(request.getEmail())
                || userRepository.existsByPhone(request.getPhone())) {
            log.info("something wrong");
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    // PUT
    // Edit user info
    @Transactional
    public User updateUser(UserUpdateRequest reqUser) {
        User dbUser = this.findById(reqUser.getId());

        if (reqUser.getFirstName() != null && !reqUser.getFirstName().isEmpty()
                && !reqUser.getFirstName().equals(dbUser.getFirstName())) {
            dbUser.setFirstName(reqUser.getFirstName());
        }

        if (reqUser.getLastName() != null && !reqUser.getLastName().isEmpty()
                && !reqUser.getLastName().equals(dbUser.getLastName())) {
            dbUser.setLastName(reqUser.getLastName());
        }

        if (reqUser.getGender() != null && !reqUser.getGender().equals(dbUser.getGender())) {
            dbUser.setGender(reqUser.getGender());
        }

        if (reqUser.getBio() != null && !reqUser.getBio().isEmpty()
                && !reqUser.getBio().equals(dbUser.getBio())) {
            dbUser.setBio(reqUser.getBio());
        }

        if (reqUser.getDob() != null && !reqUser.getDob().equals(dbUser.getDob())) {
            dbUser.setDob(reqUser.getDob());
        }

        if (reqUser.getLocation() != null && !reqUser.getLocation().isEmpty()
                && !reqUser.getLocation().equals(dbUser.getLocation())) {
            dbUser.setLocation(reqUser.getLocation());
        }

        if (reqUser.getAvatarUrl() != null && !reqUser.getAvatarUrl().isEmpty()
                && !reqUser.getAvatarUrl().equals(dbUser.getAvatarUrl())) {
            dbUser.setAvatarUrl(reqUser.getAvatarUrl());
        }
        return this.userRepository.save(dbUser);
    }

    // DELETE
    public void deleteUserById(Long id) {
        User dbUser = this.findById(id);

        userRepository.delete(dbUser);
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

    public void updateUserToken(String token, String emailUsernamePhone) {
        User currentUser = this.handleGetUserByUsernameOrEmailOrPhone(emailUsernamePhone);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmailOrUsernameOrPhone(String token, String emailUsernamePhone) {
        return this.userRepository.findByRefreshTokenAndEmailOrUsernameOrPhone(token, emailUsernamePhone)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    public User getUserByEmail(String email) {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.get();
    }

    public boolean verifyOtp(User user, String otp) {
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(), Instant.now()).getSeconds() < 60) {
            user.setOtp(otp); // Clear OTP after successful verification
            userRepository.save(user);
            return true;
        } else if (!user.getOtp().equals(otp)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        } else {
            throw new AppException(ErrorCode.EXPIRED_OTP);
        }
    }

    public User findUserProfile(Jwt jwt) {
        String email = jwt.getClaim("email");
        if (email == null) {
            throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return optionalUser.get();
    }

    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }

}
