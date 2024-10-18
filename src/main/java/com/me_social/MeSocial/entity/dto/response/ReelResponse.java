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
public class ReelResponse {
    String id;
    Long userId;
    String url;
    String content;
    Instant createdAt;
    Instant updatedAt;

    int LikeNum;
    int commentNum;
}
