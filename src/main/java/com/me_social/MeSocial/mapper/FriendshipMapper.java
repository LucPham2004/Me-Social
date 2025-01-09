package com.me_social.MeSocial.mapper;

import com.me_social.MeSocial.repository.UserRepository;
import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.response.FriendShipResponse;
import com.me_social.MeSocial.entity.modal.Friendship;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class FriendshipMapper {

    private final UserRepository userRepository;
    
    public FriendShipResponse toFriendShipResponse(Friendship friendship) {
        FriendShipResponse response = new FriendShipResponse();
        response.setFriendshipId(friendship.getId());
        response.setRequesterAvatar(friendship.getRequester().getAvatarUrl());
        response.setReceiverAvatar(friendship.getRequestReceiver().getAvatarUrl());
        response.setReceiverName(friendship.getRequestReceiver().getUsername());
        response.setRequesterName(friendship.getRequester().getUsername());
        response.setReceiverId(friendship.getRequestReceiver().getId());
        response.setRequesterId(friendship.getRequester().getId());
        response.setMutualFriend(userRepository.countMutualFriends(friendship.getRequester().getId(), friendship.getRequestReceiver().getId()));
        response.setStatus(friendship.getStatus());

        return response;
    }
}
