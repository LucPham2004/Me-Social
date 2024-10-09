package com.me_social.MeSocial.entity.dto.response;

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
public class UserDTO {
    Long id;
    String firstName;
    String lastName;
    String location;
    boolean isFriend;
    int friendNum;
    Long mutualFriendsNum;
}
