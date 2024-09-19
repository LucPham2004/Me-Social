package com.me_social.MeSocial.controller.restController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.enums.FriendshipStatus;
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

    @PostMapping("{requesterId}/{receiverId}")
    public ApiResponse<String> createFriendShip(@PathVariable Long requesterId, @PathVariable Long receiverId) {
        return friendShipService.createFriendShip(requesterId, receiverId);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteFriendShip(@PathVariable Long id) {
        return friendShipService.deleteFriendShip(id);
    }

    @PutMapping("/{id}/{status}")
    public ApiResponse<String> editFriendShipStatus(@PathVariable Long id, @PathVariable FriendshipStatus status) {
        return friendShipService.editFriendShipStatus(id, status);
    }

}
