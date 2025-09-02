package com.me_social.MeSocial.controller;

import com.me_social.MeSocial.entity.dto.response.MessageResponse;
import com.me_social.MeSocial.entity.modal.Message;
import com.me_social.MeSocial.mapper.ChatMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class RealtimeChat {

    SimpMessagingTemplate simpMessagingTemplate;
    ChatMapper mapper;
    @MessageMapping("/message")
    @SendTo("/goup/public")
    public MessageResponse receiveMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSend("/goup" + message.getChat().getId().toString(), mapper.toMessageResponse(message));

        return mapper.toMessageResponse(message);
    }
}
