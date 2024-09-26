package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Notification;
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

    public ApiResponse<Page<Notification>> getUserNotifications(Long userId, int pageNum) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, NOTIFY_PER_PAGE);

        ApiResponse<Page<Notification>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get notifications successfully");
        apiResponse.setResult(notificationRepository.findByUserId(userId, pageable));

        return apiResponse;
    }

    // Create notification
    public ApiResponse<Notification> notifyUser(String username, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setUser(userRepository.findByUsername(username).get());
        notification.setContent(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
        
        ApiResponse<Notification> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get notifications successfully");
        apiResponse.setResult(notificationRepository.save(notification));

        return apiResponse;
    }

    // Delete notification
    public ApiResponse<String> deleteNotify(Long notifyId) {
        if(!notificationRepository.existsById(notifyId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        notificationRepository.delete(notificationRepository.findById(notifyId));

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get notifications successfully");
        apiResponse.setResult("");

        return apiResponse;
    }
}
