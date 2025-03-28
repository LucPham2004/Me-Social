package com.me_social.MeSocial.controller;

import com.me_social.MeSocial.entity.modal.User;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.request.UserUpdateRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.UserDTO;
import com.me_social.MeSocial.entity.dto.response.UserResponse;
import com.me_social.MeSocial.mapper.UserMapper;
import com.me_social.MeSocial.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    // POST
    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest reqUser) {
        var user = this.userService.createUser(reqUser);
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Create user successfully!")
                .result(userMapper.toUserResponse(user))
                .build();
    }

    // GET

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        var dbUser = this.userService.getUser(id);
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Get user with ID " + id + " successfully!")
                .result(userMapper.toUserResponse(dbUser))
                .build();
    }

    // PUT
    @PutMapping(value = "/updateUser", consumes = "application/json", produces = "application/json")
    public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest reqUser) {
        var user = this.userService.updateUser(reqUser);

        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Update user with ID " + reqUser.getId() + " successfully")
                .result(userMapper.toUserResponse(user))
                .build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUserById(@PathVariable Long id) {
        this.userService.deleteUserById(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete user with ID " + id + " successfully!")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<UserDTO>> getAllUsers(@Filter Specification<User> spec, Pageable pageable) {
        var users = this.userService.getAllUsers(spec, pageable);
        return ApiResponse.<Page<UserDTO>>builder()
                .code(1000)
                .message("Get all users successfully!")
                .result(users.map(userMapper::toUserDTO))
                .build();
    }

    // Get Group members
    @GetMapping("/get/group/members")
    public ApiResponse<Page<UserDTO>> getGroupMembers(@RequestParam Long groupId, Pageable pageable) {

        var members = userService.getGroupMembers(groupId, pageable);

        return ApiResponse.<Page<UserDTO>>builder()
                .code(1000)
                .message("Get members of the group with ID " + groupId + " successfully!")
                .result(members.map(userMapper::toUserDTO))
                .build();
    }

    // Get Group admins
    @GetMapping("/get/group/admins")
    public ApiResponse<Page<UserDTO>> getGroupAdmins(@RequestParam Long groupId, Pageable pageable) {
        var admins = this.userService.getGroupAdmins(groupId, pageable);

        return ApiResponse.<Page<UserDTO>>builder()
                .code(1000)
                .message("Get admins of the group with ID " + groupId + " successfully!")
                .result(admins.map(userMapper::toUserDTO))
                .build();
    }

    // Get User's friends
    @GetMapping("/get/friends")
    public ApiResponse<Page<UserDTO>> getUserFriends(
            @RequestParam Long userId, Pageable pageable) {
        var friends = this.userService.getUserFriends(userId, pageable);
        return ApiResponse.<Page<UserDTO>>builder()
                .code(1000)
                .message("Get friends of the user with ID " + userId + " successfully!")
                .result(friends)
                .build();
    }

    // Get mutual friends
    @GetMapping("/get/mutualFriends")
    public ApiResponse<Page<UserDTO>> getMutualFriends(
            @RequestParam Long meId,
            @RequestParam Long youId,
            Pageable pageable) {
        var friends = this.userService.getMutualFriends(meId, youId, pageable);
        return ApiResponse.<Page<UserDTO>>builder()
                .code(1000)
                .message("Get mutual friends of user with ID " + meId +
                        " and user with ID " + youId + " successfully!")
                .result(friends)
                .build();
    }

    // Get User's friends
    @GetMapping("/get/friends/suggested")
    public ApiResponse<Page<UserDTO>> getSuggestedFriends(
            @RequestParam Long userId,
            Pageable pageable) {
        var friends = this.userService.getSuggestedFriends(userId, pageable);
        return ApiResponse.<Page<UserDTO>>builder()
                .code(1000)
                .message("Get suggested friends of the user with ID " + userId + " successfully!")
                .result(friends)
                .build();
    }

    @GetMapping("/search/{query}")
    public ApiResponse<List<UserDTO>> searchUser(@PathVariable String query) {
        List<User> users = userService.searchUser(query);
        var userDTO = users.stream().map(userMapper::toUserDTO).toList();

        return ApiResponse.<List<UserDTO>>builder()
                .code(1000)
                .message("querying")
                .result(userDTO)
                .build();
    }
}
