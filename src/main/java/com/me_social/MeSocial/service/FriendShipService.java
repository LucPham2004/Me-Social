package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Friendship;
import com.me_social.MeSocial.enums.FriendshipStatus;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.FriendShipRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendShipService {
    FriendShipRepository friendShipRepository;
    UserService userService;
    UserRepository userRepository;

    public ApiResponse<String> createFriendShip(Long requesterId, Long receiverId) {
        if (!userRepository.existsById(requesterId) || !userRepository.existsById(receiverId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Friendship friendship = new Friendship();
        friendship.setRequester(userService.findById(requesterId).get());
        friendship.setRequestReceiver(userService.findById(receiverId).get());
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setCreatedAt(LocalDateTime.now());

        friendShipRepository.save(friendship);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Sent friendship request successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    public ApiResponse<String> deleteFriendShip(Long id) {
        if (!friendShipRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        friendShipRepository.delete(friendShipRepository.findById(id));

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("delete friendship successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    @Transactional
    public ApiResponse<String> editFriendShipStatus(Long id, FriendshipStatus status) {
        Friendship friendship = friendShipRepository.findById(id);
        if (friendship == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        friendship.setStatus(status);
        friendship.setUpdatedAt(LocalDateTime.now());
        friendShipRepository.save(friendship);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("edit friendship successfully");
        apiResponse.setResult("");

        return apiResponse;
    }
}
