package com.me_social.MeSocial.entity.dto.request;

import com.me_social.MeSocial.enums.PostPrivacy;

import jakarta.validation.constraints.NotBlank;
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
    String content;
    PostPrivacy privacy;
    
    @NotBlank
    Long userId;
    Long groupId;
    String[] nameTag;
}
