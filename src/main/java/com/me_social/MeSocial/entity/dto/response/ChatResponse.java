package com.me_social.MeSocial.entity.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponse {
    private Long id;
    private String chatName;
    private String chatImage;
    private boolean isGroup;

    private UserResponse createdBy; // Include full user info
    private Set<UserResponse> admins;
    private Set<UserResponse> users;
    private List<MessageResponse> messages;
}
