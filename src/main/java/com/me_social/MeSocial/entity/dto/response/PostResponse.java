package com.me_social.MeSocial.entity.dto.response;

import java.time.LocalDateTime;

import com.me_social.MeSocial.enums.PostPrivacy;

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
public class PostResponse {
    Long id;
    Long userId;
    Long groupId;
    String content;
    PostPrivacy privacy;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    int likeNum;
    int commentNum;
}
