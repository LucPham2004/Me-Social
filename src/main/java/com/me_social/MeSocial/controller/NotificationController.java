package com.me_social.MeSocial.controller;

import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Notification;
import com.me_social.MeSocial.service.NotificationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class NotificationController {

    SimpMessagingTemplate messagingTemplate;
    NotificationService notificationService;

    // Get User Notifications
    @GetMapping("/")
    public ApiResponse<Page<Notification>> getUserNotifications(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int pageNum) {
        Page<Notification> notifications = notificationService.getUserNotifications(userId, pageNum);
        return ApiResponse.<Page<Notification>>builder()
            .code(1000)
            .message("Get notifications successfully")
            .result(notifications)
            .build();
    }

    @MessageMapping("/send-notification")
    public void sendNotification(Notification notification) {
        String username = notification.getUser().getUsername(); 
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
    }

    // Delete Notification
    @DeleteMapping("/{notifyId}")
    public ApiResponse<String> deleteNotify(@PathVariable Long notifyId) {
        notificationService.deleteNotify(notifyId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Delete notification successfully")
            .result("")
            .build();
    }
}