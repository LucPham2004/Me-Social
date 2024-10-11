package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.ChatResponse;
import com.me_social.MeSocial.entity.modal.DirectMessage;
import com.me_social.MeSocial.mapper.ChatMapper;
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
    ChatMapper chatMapper;
    
    // Get messages
    @GetMapping("/")
    public ApiResponse<Page<ChatResponse>> getUserDirectMessages(Long senderId, Long receiverId, int pageNum) {
        Page<DirectMessage> messages = service.getUserDirectMessages(senderId, receiverId, pageNum);
        return ApiResponse.<Page<ChatResponse>>builder()
            .code(1000)
            .message("Get messages by sender with id: " + senderId + " and receiver with id: " + receiverId + " successfully")
            .result(messages.map(chatMapper::toResponse))
            .build();
    }

    // Delete message
    @DeleteMapping("/{notifyId}")
    public ApiResponse<Void> deleteDirectMessage(Long messageId) {
        this.service.deleteDirectMessage(messageId);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete message with ID " + messageId + " successfully!")
            .build();
    }
}
