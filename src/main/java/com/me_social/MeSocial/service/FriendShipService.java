package com.me_social.MeSocial.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Friendship getFriendStatus(Long requesterId, Long receiverId) {
        if (!userRepository.existsById(requesterId) || !userRepository.existsById(receiverId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Friendship friendship = friendShipRepository.findBy2UserIds(requesterId, receiverId);

        return friendship;
    }

    public Friendship sendFriendRequest(Long requesterId, Long receiverId) {
        if (!userRepository.existsById(requesterId) || !userRepository.existsById(receiverId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Friendship friendship = friendShipRepository.findBy2UserIds(requesterId, receiverId);
        if(friendship != null) {
            return friendship;
        }
        friendship = new Friendship();
        friendship.setRequester(userService.findById(requesterId).get());
        friendship.setRequestReceiver(userService.findById(receiverId).get());
        friendship.setStatus(FriendshipStatus.PENDING);

        return friendShipRepository.save(friendship);
    }

    public Page<Friendship> getFriendRequestByUser (Long userId, int pageNum) {

        if (this.userService.findById(userId).isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Pageable pageable = PageRequest.of(pageNum, 20);

        return this.friendShipRepository.findFriendRequestByUser(userId, pageable);
    }

    public void deleteFriendShip(Long id) {
        if (!friendShipRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        friendShipRepository.delete(friendShipRepository.findById(id).get());
    }

    @Transactional
    public Friendship editFriendShipStatus(Long id, FriendshipStatus status) {
        Friendship friendship = friendShipRepository.findById(id).get();

        friendship.setStatus(status);
        return friendShipRepository.save(friendship);
    }
}
