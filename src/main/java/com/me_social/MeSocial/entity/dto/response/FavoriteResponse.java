package com.me_social.MeSocial.entity.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class FavoriteResponse {
    Long id;
    Long userId;
    String username;
    PostResponse post;
    Instant createdAt;
}
