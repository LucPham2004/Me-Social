package com.me_social.MeSocial.controller.restController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.FriendShipResponse;
import com.me_social.MeSocial.enums.FriendshipStatus;
import com.me_social.MeSocial.mapper.FriendshipMapper;
import com.me_social.MeSocial.service.FriendShipService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class FriendShipController {
    FriendShipService friendShipService;
    FriendshipMapper mapper;

    @GetMapping("{requesterId}/{receiverId}")
    public ApiResponse<FriendShipResponse> getFriendStatus(@PathVariable Long requesterId, @PathVariable Long receiverId) {
            var response = this.friendShipService.getFriendStatus(requesterId, receiverId);
            if(response == null) {
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

}
