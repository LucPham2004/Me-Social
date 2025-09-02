package com.me_social.MeSocial.entity.dto.response;

import com.me_social.MeSocial.entity.modal.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private UserResponse sender;
    private Long chatId;
}

