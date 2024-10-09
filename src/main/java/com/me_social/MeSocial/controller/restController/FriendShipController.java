package com.me_social.MeSocial.controller.restController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Friendship;
import com.me_social.MeSocial.enums.FriendshipStatus;
import com.me_social.MeSocial.service.FriendShipService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendShipController {
    FriendShipService friendShipService;

    @PostMapping("{requesterId}/{receiverId}")
    public ApiResponse<Friendship> createFriendShip(@PathVariable Long requesterId, @PathVariable Long receiverId) {
        var friendShip = friendShipService.createFriendShip(requesterId, receiverId);
        return ApiResponse.<Friendship>builder()
                .code(1000)
                .message("just send a friend request to user with ID " + receiverId + " successfully!")
                .result(friendShip)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFriendShip(@PathVariable Long id) {
        var friendShip = friendShipService.findById(id).get();
        this.friendShipService.deleteFriendShip(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete friend ship with ID " + friendShip.getId() + " successfully!")
                .build();
    }

    @PutMapping("/{id}/{status}")
    public ApiResponse<Friendship> editFriendShipStatus(@PathVariable Long id, @PathVariable FriendshipStatus status) {
        var friendShip = friendShipService.editFriendShipStatus(id, status);
        return ApiResponse.<Friendship>builder().code(1000).message("Update friendship status successfully!")
                .result(friendShip).build();
    }

}
