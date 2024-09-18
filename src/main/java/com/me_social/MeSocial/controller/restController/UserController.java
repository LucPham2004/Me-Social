package com.me_social.MeSocial.controller.restController;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.UserCreationResponse;
import com.me_social.MeSocial.entity.dto.response.UserResponse;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<UserCreationResponse> createUser(@RequestBody UserCreationRequest reqUser) {
        return userService.createUser(reqUser);
    }

    // @GetMapping("/${id}")
    // public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
    //     return userService.getUser(id);
    // }

    // GET
    // Get Group members
    @GetMapping("/get/group/members/{groupId}-{pageNum}")
    public ApiResponse<Page<User>> getGroupMembers(@PathVariable Long groupId, @PathVariable int pageNum) {
        return userService.getGroupMembers(groupId, pageNum);
    }

    // Get Group admins
    @GetMapping("/get/group/admins/{groupId}-{pageNum}")
    public ApiResponse<Set<User>> getGroupAdmins(@PathVariable Long groupId, @PathVariable int pageNum) {
        return userService.getGroupAdmins(groupId, pageNum);
    }

    // Get User's followers
    @GetMapping("/get/followers/{userId}-{pageNum}")
    public ApiResponse<Page<User>> getFollowers(@PathVariable Long userId, @PathVariable int pageNum) {
        return userService.getFollowers(userId, pageNum);
    }

    // Get User's followers
    @GetMapping("/get/followings/{userId}-{pageNum}")
    public ApiResponse<Page<User>> getFollowings(@PathVariable Long userId, @PathVariable int pageNum) {
        return userService.getFollowings(userId, pageNum);
    }
}
