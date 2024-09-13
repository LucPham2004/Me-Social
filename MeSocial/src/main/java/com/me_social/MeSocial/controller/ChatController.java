package com.me_social.MeSocial.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.me_social.MeSocial.entity.modal.DirectMessage;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void processMessage(DirectMessage directMessage) {
        // Gửi tin nhắn tới người nhận (receiver)
        messagingTemplate.convertAndSendToUser(directMessage.getReceiver().getUsername(), "/queue/messages", directMessage);
    }
}

