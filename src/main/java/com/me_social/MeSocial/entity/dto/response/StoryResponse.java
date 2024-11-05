package com.me_social.MeSocial.entity.dto.response;

import java.time.Instant;

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
public class StoryResponse {
    String id;
    Long userId;
    String content;

    String thumbnail;
    String[] urls;
    String[] publicIds;

    Instant createdAt;
    Instant updatedAt;

    int LikeNum;
}
