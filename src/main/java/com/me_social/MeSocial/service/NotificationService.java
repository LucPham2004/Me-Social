package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.modal.Notification;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.enums.NotificationType;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.NotificationRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class NotificationService {

    SimpMessagingTemplate messagingTemplate;
    NotificationRepository notificationRepository;
    UserRepository userRepository;

    static int NOTIFY_PER_PAGE = 10;

    // Get User Notifications
    public Page<Notification> getUserNotifications(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, NOTIFY_PER_PAGE);
        return notificationRepository.findByUserId(userId, pageable);
    }

    // Create Notification
    public Notification notifyUser(String username, String message, NotificationType type) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
        return notificationRepository.save(notification);
    }

    // Delete Notification
    public void deleteNotify(Long notifyId) {
        if (!notificationRepository.existsById(notifyId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        notificationRepository.deleteById(notifyId);
    }
}
