package com.me_social.MeSocial.entity.dto.response;

import java.time.LocalDateTime;

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
public class ChatResponse {
    Long id;
    Long senderId;
    Long receiverId;
    String content;
    Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
