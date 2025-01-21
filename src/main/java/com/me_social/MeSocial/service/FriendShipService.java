package com.me_social.MeSocial.service;

import com.me_social.MeSocial.entity.dto.response.FriendShipResponse;
import com.me_social.MeSocial.mapper.FriendshipMapper;
import lombok.extern.slf4j.Slf4j;
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

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FriendShipService {
    FriendShipRepository friendShipRepository;
    UserService userService;
    UserRepository userRepository;
    FriendshipMapper friendshipMapper;

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
        if (friendship != null) {
            return friendship;
        }
        friendship = new Friendship();
        friendship.setRequester(userService.findById(requesterId).get());
        friendship.setRequestReceiver(userService.findById(receiverId).get());
        friendship.setStatus(FriendshipStatus.PENDING);

        return friendShipRepository.save(friendship);
    }

    public Page<FriendShipResponse> getUserFriends(Long userId, Pageable pageable) {

        var friendships = friendShipRepository.findUserFriends(userId, pageable);

        return getFriendshipsWithMutualFriendsCount(userId, friendships);
    }



    private Page<FriendShipResponse> getFriendshipsWithMutualFriendsCount(Long userId, Page<Friendship> friendships) {
        List<Long> userIds = new ArrayList<>();
        for (Friendship friendship : friendships.getContent()) {
            if (Objects.equals(friendship.getRequester().getId(), userId)) {
                userIds.add(friendship.getRequestReceiver().getId());
            } else {
                userIds.add(friendship.getRequester().getId());
            }
        }

        Map<Long, Long> mutualFriendsCount = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<Object[]> result = userRepository.countMutualFriendsForUsers2(userId, userIds);
            for (Object[] row : result) {
                Long otherUserId = (Long) row[0];
                Long count = (Long) row[1];
                mutualFriendsCount.put(otherUserId, count);
            }
        }

        return friendships.map(friendship -> {
            FriendShipResponse response = friendshipMapper.toFriendShipResponse(friendship);
            Long otherUserId = Objects.equals(friendship.getRequester().getId(), userId)
                    ? friendship.getRequestReceiver().getId()
                    : friendship.getRequester().getId();
            response.setMutualFriend(mutualFriendsCount.getOrDefault(otherUserId, 0L));
            return response;
        });
    }


    public Page<Friendship> getFriendRequestByUser(Long userId, Pageable pageable) {

        if (this.userService.findById(userId).isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

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

        if (status == FriendshipStatus.ACCEPTED) {
            friendship.setAcceptedAt(Instant.now());
        }

        friendship.setStatus(status);
        return friendShipRepository.save(friendship);
    }

    public Page<FriendShipResponse> getUserFriendsTest(Long userId, Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 20);
        log.info("pageNum: {}", pageNum);

        return friendShipRepository.findUserFriends(userId, pageable).map(friendshipMapper::toFriendShipResponse);
    }

    public List<FriendShipResponse> getRecentAcceptedFriendships(Long userId) {
        Instant oneHourAgo = Instant.now().minusSeconds(3600);
        List<Friendship> recentFriendships = friendShipRepository.findRecentCreatedFriendshipsByUserId(userId, oneHourAgo);

        // Map to response DTO
        return recentFriendships.stream()
                .map(friendshipMapper::toFriendShipResponse)
                .collect(Collectors.toList());
    }

}
