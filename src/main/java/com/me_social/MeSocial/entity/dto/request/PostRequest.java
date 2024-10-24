package com.me_social.MeSocial.entity.dto.request;

import com.me_social.MeSocial.enums.PostPrivacy;

import jakarta.validation.constraints.NotNull;
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
public class PostRequest {
    Long id;

    @NotNull
    Long userId;
    Long groupId;
    String content;
    String[] urls;
    String[] publicIds;
    
    @NotNull
    PostPrivacy privacy;
    
    String[] nameTag;
}
