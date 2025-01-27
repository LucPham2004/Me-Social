package com.me_social.MeSocial.entity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class SendMessageRequest {
    Long userId;
    Long chatId;
    String content;
}
