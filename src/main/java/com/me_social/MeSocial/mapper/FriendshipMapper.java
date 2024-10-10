package com.me_social.MeSocial.mapper;

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
    
    public FriendShipResponse toFriendShipResponse(Friendship friendship) {
        FriendShipResponse response = new FriendShipResponse();
        response.setFriendshipId(friendship.getId());
        response.setReceiverId(friendship.getRequestReceiver().getId());
        response.setRequesterId(friendship.getRequester().getId());
        response.setStatus(friendship.getStatus());

        return response;
    }
}
