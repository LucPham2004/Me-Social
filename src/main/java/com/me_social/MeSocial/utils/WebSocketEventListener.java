// package com.me_social.MeSocial.utils;

// import org.springframework.context.event.EventListener;
// import org.springframework.messaging.simp.SimpMessageSendingOperations;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.messaging.SessionConnectedEvent;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Component
// @Slf4j
// @RequiredArgsConstructor
// public class WebSocketEventListener {

//     private final SimpMessageSendingOperations messagingTemplate;

//     @EventListener
//     public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//         log.info("Received a new web socket connection");
//     }
    
//     // @EventListener
//     // public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//     //     StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//     //     long senderId = (long) headerAccessor.getSessionAttributes().get("senderId");
//     //     long receiverId = (long) headerAccessor.getSessionAttributes().get("receiverId");
        
//     //         log.info("user disconnected: {}", senderId);
            
//     //         var chatMessage = ChatMessage.builder()
//     //                 .type(MessageType.LEAVE)
//     //                 .senderId(senderId)
//     //                 .receiverId(receiverId)
//     //                 .build();
            
//     //         messagingTemplate.convertAndSend("/topic/public", chatMessage);
//     //     }
    
// }
