package com.me_social.MeSocial.entity.dto.request;

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
public class StoryRequest {
    Long userId;
    String[] urls;
    String[] publicIds;
    String content;
    String thumbnail;
}
