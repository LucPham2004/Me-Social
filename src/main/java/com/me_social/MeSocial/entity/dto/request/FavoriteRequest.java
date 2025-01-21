package com.me_social.MeSocial.entity.dto.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.entity.modal.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class FavoriteRequest {
    Long userId;
    Long postId;
}
