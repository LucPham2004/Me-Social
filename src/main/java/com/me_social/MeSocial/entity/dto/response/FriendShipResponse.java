package com.me_social.MeSocial.entity.dto.response;

import com.me_social.MeSocial.enums.FriendshipStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class FriendShipResponse {
    Long friendshipId;
    Long requesterId;
    Long receiverId;
    String requesterName;
    String receiverName;
    String requesterAvatar;
    String receiverAvatar;
    Long mutualFriend;
    FriendshipStatus status;
}
