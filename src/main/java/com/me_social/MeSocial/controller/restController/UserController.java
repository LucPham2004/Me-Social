package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
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
import com.me_social.MeSocial.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // POST
    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest reqUser) {
        return userService.createUser(reqUser);
    }

    // GET

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // Get Group members
    @GetMapping("/get/group/members/{groupId}/{pageNum}")
    public ApiResponse<Page<UserDTO>> getGroupMembers(
        @RequestParam Long groupId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return userService.getGroupMembers(groupId, pageNum);
    }

    // Get Group admins
    @GetMapping("/get/group/admins/{groupId}/{pageNum}")
    public ApiResponse<Page<UserDTO>> getGroupAdmins(
        @RequestParam Long groupId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return userService.getGroupAdmins(groupId, pageNum);
    }

    // Get User's friends
    @GetMapping("/get/friends/{userId}/{pageNum}")
    public ApiResponse<Page<UserDTO>> getUserFriends(
        @RequestParam Long userId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return userService.getUserFriends(userId, pageNum);
    }

    // PUT
    @PutMapping(value = "/updateUser", consumes = "application/json", produces = "application/json")
    public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest reqUser) {
        return userService.updateUser(reqUser);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping
    public ApiResponse<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getAllUsers(page, size);
    }

}
