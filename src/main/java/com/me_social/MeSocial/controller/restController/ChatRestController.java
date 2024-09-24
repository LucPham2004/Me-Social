package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.DirectMessage;
import com.me_social.MeSocial.service.DirectMessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ChatRestController {

    DirectMessageService service;
    
    // Get messages
    @GetMapping("/{userId}/{pageNum}")
    public ApiResponse<Page<DirectMessage>> getUserDirectMessages(Long senderId, Long receiverId, int pageNum) {
        return service.getUserDirectMessages(senderId, receiverId, pageNum);
    }

    // Delete message
    @DeleteMapping("/{notifyId}")
    public ApiResponse<String> deleteDirectMessage(Long messageId) {
        return service.deleteDirectMessage(messageId);
    }
}
