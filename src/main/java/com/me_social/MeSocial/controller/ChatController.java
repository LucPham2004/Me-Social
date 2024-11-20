// package com.me_social.MeSocial.controller;

// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.Payload;
// import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Controller;

// import com.me_social.MeSocial.entity.modal.ChatMessage;
// import com.me_social.MeSocial.mapper.ChatMapper;
// import com.me_social.MeSocial.service.DirectMessageService;

// import lombok.AccessLevel;
// import lombok.RequiredArgsConstructor;
// import lombok.experimental.FieldDefaults;

// @Controller
// @RequiredArgsConstructor
// @FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
// public class ChatController {

//     ChatMapper chatMapper;
//     DirectMessageService chatService;
//     SimpMessagingTemplate messagingTemplate;

//     @MessageMapping("/sendMessage")
//     @SendTo("/topic/public")
//     public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//         chatService.saveDirectMessage(chatMapper.toDirectMessage(chatMessage));

//         return chatMessage;
        
//     }

//     @MessageMapping("/private-message")
//     public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
//         chatService.saveDirectMessage(chatMapper.toDirectMessage(chatMessage));
        
//         String receiverId = chatMessage.getReceiverId().toString();
//         messagingTemplate.convertAndSendToUser(receiverId, "/queue/messages", chatMessage);
//     }
// }

