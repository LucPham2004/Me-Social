package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
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

    public Optional<Friendship> findById(Long id) {
        var optionalFriendShip = this.friendShipRepository.findById(id);
        if (optionalFriendShip.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        return optionalFriendShip;
    }

    public Friendship createFriendShip(Long requesterId, Long receiverId) {
        var requester = this.userService.findById(requesterId).get();
        var receiver = this.userService.findById(receiverId).get();

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setRequestReceiver(receiver);
        friendship.setStatus(FriendshipStatus.PENDING);

        return friendShipRepository.save(friendship);
    }

    public void deleteFriendShip(Long id) {
        var friendShip = this.findById(id).get();
        this.friendShipRepository.delete(friendShip);
    }

    @Transactional
    public Friendship editFriendShipStatus(Long id, FriendshipStatus status) {
        Friendship friendship = friendShipRepository.findById(id).get();

        friendship.setStatus(status);
        return friendShipRepository.save(friendship);
    }
}
