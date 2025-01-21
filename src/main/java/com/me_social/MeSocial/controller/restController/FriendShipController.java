package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.FriendShipResponse;
import com.me_social.MeSocial.enums.FriendshipStatus;
import com.me_social.MeSocial.mapper.FriendshipMapper;
import com.me_social.MeSocial.service.FriendShipService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendShipController {
    FriendShipService friendShipService;
    FriendshipMapper mapper;

    @GetMapping("{requesterId}/{receiverId}")
    public ApiResponse<FriendShipResponse> getFriendStatus(@PathVariable Long requesterId, @PathVariable Long receiverId) {
        var response = this.friendShipService.getFriendStatus(requesterId, receiverId);
        if (response == null) {
            return ApiResponse.<FriendShipResponse>builder()
                    .code(1000)
                    .message("2 Users are not Friends")
                    .build();
        }
        return ApiResponse.<FriendShipResponse>builder()
                .code(1000)
                .message("Get friendship status from user with ID: " + requesterId +
                        " and user with ID: " + receiverId + " successfully!")
                .result(mapper.toFriendShipResponse(response))
                .build();
    }

    @GetMapping("/friendRequest")
    public ApiResponse<Page<FriendShipResponse>> getFriendRequestByUser(@RequestParam Long userId, Pageable pageable) {
        var response = this.friendShipService.getFriendRequestByUser(userId, pageable);

        return ApiResponse.<Page<FriendShipResponse>>builder()
                .code(1000)
                .message("Get friend requests for user with id = " + userId + " successfully!")
                .result(response.map(mapper::toFriendShipResponse))
                .build();
    }

    @PostMapping("{requesterId}/{receiverId}")
    public ApiResponse<FriendShipResponse> sendFriendRequest(@PathVariable Long requesterId, @PathVariable Long receiverId) {
        var response = this.friendShipService.sendFriendRequest(requesterId, receiverId);
        return ApiResponse.<FriendShipResponse>builder()
                .code(1000)
                .message("Send friend request from user with ID: " + requesterId +
                        " to user with ID: " + receiverId + " successfully!")
                .result(mapper.toFriendShipResponse(response))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFriendShip(@PathVariable Long id) {
        this.friendShipService.deleteFriendShip(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete friendship successfully!")
                .build();
    }

    @PutMapping("/{id}/{status}")
    public ApiResponse<FriendShipResponse> editFriendShipStatus(@PathVariable Long id, @PathVariable FriendshipStatus status) {
        var response = this.friendShipService.editFriendShipStatus(id, status);
        return ApiResponse.<FriendShipResponse>builder()
                .code(1000)
                .message("Edit friendship status successfully!")
                .result(mapper.toFriendShipResponse(response))
                .build();
    }

    @GetMapping("/friends")
    public ApiResponse<Page<FriendShipResponse>> getUserFriends(@RequestParam Long userId, Pageable pageable) {
        var response = this.friendShipService.getUserFriends(userId, pageable);
        return ApiResponse.<Page<FriendShipResponse>>builder()
                .code(1000)
                .message("Get friends for user " + userId + " successfully!")
                .result(response)
                .build();
    }

    @GetMapping("/friendsTest/{userId}/{pageNum}")
    public ApiResponse<Page<FriendShipResponse>> getUserFriendsTest(
            @PathVariable Long userId,
            @PathVariable(required = false) Integer pageNum) {

        // Provide default value if pageNum is null
        if (pageNum == null) {
            pageNum = 0; // Default to the first page
        }

        var response = this.friendShipService.getUserFriendsTest(userId, pageNum);

        return ApiResponse.<Page<FriendShipResponse>>builder()
                .code(1000)
                .message("get user friends test")
                .result(response)
                .build();
    }

    @GetMapping("/currentAccepted/{userId}")
    public ApiResponse<List<FriendShipResponse>> getCurrentAcceptedFriendship(@PathVariable Long userId) {
        var response = friendShipService.getRecentAcceptedFriendships(userId);

        return ApiResponse.<List<FriendShipResponse>>builder()
                .code(1000)
                .message("Get current accept friendship for user with id = " + userId + " successfully!")
                .result(response)
                .build();
    }

}
