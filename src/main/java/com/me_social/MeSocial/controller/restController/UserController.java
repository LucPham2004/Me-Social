package com.me_social.MeSocial.controller.restController;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest reqUser) {
        return userService.createUser(reqUser);
    }  

    // GET
    
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping(value = "/updateUser", consumes = "application/json", produces = "application/json")
    public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest reqUser) {
        return userService.updateUser(reqUser);
    }

    // GET
    // Get Group members

    @GetMapping("/get/group/members/{groupId}/{pageNum}")
    public ApiResponse<Page<UserDTO>> getGroupMembers(@PathVariable Long groupId, @PathVariable int pageNum) {
        return userService.getGroupMembers(groupId, pageNum);
    }

    // Get Group admins
    @GetMapping("/get/group/admins/{groupId}/{pageNum}")
    public ApiResponse<Set<UserDTO>> getGroupAdmins(@PathVariable Long groupId, @PathVariable int pageNum) {
        return userService.getGroupAdmins(groupId, pageNum);
    }

    // Get User's friends
    @GetMapping("/get/friends/{userId}/{pageNum}")
    public ApiResponse<Page<UserDTO>> getUserFriends(@PathVariable Long userId, @PathVariable int pageNum) {
        return userService.getUserFriends(userId, pageNum);
    }






















    // // Get User's followers
    // @GetMapping("/get/followers/{userId}/{pageNum}")
    // public ApiResponse<Page<User>> getFollowers(@PathVariable Long userId, @PathVariable int pageNum) {
    //     return userService.getFollowers(userId, pageNum);
    // }

    // // Get User's followers
    // @GetMapping("/get/followings/{userId}/{pageNum}")
    // public ApiResponse<Page<User>> getFollowings(@PathVariable Long userId, @PathVariable int pageNum) {
    //     return userService.getFollowings(userId, pageNum);
    // }
}
