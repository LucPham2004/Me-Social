package com.me_social.MeSocial.controller;

import com.me_social.MeSocial.entity.dto.request.SendMessageRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.MessageResponse;
import com.me_social.MeSocial.entity.modal.Message;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.mapper.ChatMapper;
import com.me_social.MeSocial.service.MessageService;
import com.me_social.MeSocial.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;
    UserService userService;
    ChatMapper chatMapper;

    @PostMapping("/create")
    ApiResponse<MessageResponse> sendMessage(@RequestBody SendMessageRequest request, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.findUserProfile(jwt);
        request.setUserId(request.getUserId());
        Message message = messageService.sendMessage(request);

        return ApiResponse.<MessageResponse>builder()
                .code(1000)
                .message("send message successfully!")
                .result(chatMapper.toMessageResponse(message))
                .build();
    }

    @GetMapping("/chat/{chatId}")
    ApiResponse<List<MessageResponse>> getChatsMessages(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.findUserProfile(jwt);

        List<Message> messages = messageService.getChatsMessages(chatId, user);

        return ApiResponse.<List<MessageResponse>>builder()
                .code(1000)
                .message("send message successfully!")
                .result(messages.stream().map(chatMapper::toMessageResponse).toList())
                .build();
    }

    @DeleteMapping("/{messageId}")
    ApiResponse<Void> deleteMessage(@PathVariable Long messageId, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.findUserProfile(jwt);

        messageService.deleteMessage(messageId, user);

        return ApiResponse.<Void>builder()
                .code(1000)
                .message("send message successfully!")
                .build();
    }
}
