package com.me_social.MeSocial.entity.dto.response;

import java.time.Instant;
import java.util.Set;

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
public class CommentResponse {
    Long id;
    Long userId;
    Long postId;
    String content;
    Set<String> urls;
    Instant createdAt;
    Instant updatedAt;
    int likeNum;
}
