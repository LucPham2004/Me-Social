package com.me_social.MeSocial.controller.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/notify")
    public String notifyUsers(@RequestBody String message) {
        // Send messages over WebSocket to clients at /topic/notifications
        messagingTemplate.convertAndSend("/topic/notifications", message);
        return "Notification sent!";
    }
}