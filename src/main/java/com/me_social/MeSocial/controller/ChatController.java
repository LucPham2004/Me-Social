package com.me_social.MeSocial.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.me_social.MeSocial.entity.dto.request.ChatRequest;
import com.me_social.MeSocial.entity.modal.DirectMessage;
import com.me_social.MeSocial.mapper.ChatMapper;
import com.me_social.MeSocial.service.DirectMessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ChatController {

    ChatMapper chatMapper;
    DirectMessageService chatService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-message")
    @SendTo("/topic/messages")
    public DirectMessage sendMessage(ChatRequest request) {
        DirectMessage message = chatMapper.toDirectMessage(request);
        
        messagingTemplate.convertAndSend("/topic/messages/" + request.getReceiverId(), message);

        return chatService.saveDirectMessage(message);
    }
}

