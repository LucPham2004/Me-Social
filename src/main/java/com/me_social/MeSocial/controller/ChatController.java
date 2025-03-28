package com.me_social.MeSocial.controller;

import com.me_social.MeSocial.entity.dto.request.GroupChatRequest;
import com.me_social.MeSocial.entity.dto.request.SingleChatRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.ChatResponse;
import com.me_social.MeSocial.entity.modal.Chat;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.mapper.ChatMapper;
import com.me_social.MeSocial.service.ChatService;
import com.me_social.MeSocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final ChatMapper chatMapper;

    @PostMapping("/single")
    ApiResponse<ChatResponse> createChat(@RequestBody SingleChatRequest singleChatRequest, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);

        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());

        return  ApiResponse.<ChatResponse>builder()
                .code(1000)
                .message("create chat successfully!")
                .result(chat == null ? null : chatMapper.toChatResponse(chat))
                .build();
    }

    @PostMapping("/group")
    ApiResponse<ChatResponse> createGroup(@RequestBody GroupChatRequest groupChatRequest, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);

        Chat chat = chatService.createGroup(groupChatRequest, reqUser);

        return  ApiResponse.<ChatResponse>builder()
                .code(1000)
                .message("create group chat successfully!")
                .result(chatMapper.toChatResponse(chat))
                .build();
    }

    @GetMapping("/{chatId}")
    ApiResponse<ChatResponse> findChatById(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        Chat chat = chatService.findChatById(chatId);

        return  ApiResponse.<ChatResponse>builder()
                .code(1000)
                .message("get chat with id " + chatId + " successfully!")
                .result(chatMapper.toChatResponse(chat))
                .build();
    }

    @GetMapping("/user")
    ApiResponse<List<ChatResponse>> findAllChatsByUser(@AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);
        List<Chat> chatList = chatService.findAllChatByUserId(reqUser.getId());

        return  ApiResponse.<List<ChatResponse>>builder()
                .code(1000)
                .message("get all chats for user with email " + reqUser.getEmail() + " successfully!")
                .result(chatList.stream().map(chatMapper::toChatResponse).toList())
                .build();
    }

    @GetMapping("/{userId1}/{userId2}")
    ApiResponse<ChatResponse> findChatByUserIds(@PathVariable Long userId1, @PathVariable Long userId2) {
        var chat = chatService.findChatBy2Users(userId1, userId2);

        return ApiResponse.<ChatResponse>builder()
                .code(1000)
                .message("Get chat successfully!")
                .result(chat == null ? null : chatMapper.toChatResponse(chat))
                .build();
    }

    @PutMapping("/{chatId}/add/{userId}")
    ApiResponse<ChatResponse> addUserToGroupChat(@PathVariable Long chatId, @PathVariable Long userId, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(userId, chatId, reqUser);

        return  ApiResponse.<ChatResponse>builder()
                .code(1000)
                .message("add user to group chat successfully!")
                .result(chatMapper.toChatResponse(chat))
                .build();
    }

    @PutMapping("/{chatId}/remove/{userId}")
    ApiResponse<ChatResponse> removeUserFromGroupChat(@PathVariable Long chatId, @PathVariable Long userId, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(userId, chatId, reqUser);

        return  ApiResponse.<ChatResponse>builder()
                .code(1000)
                .message("remove user from group successfully!")
                .result(chatMapper.toChatResponse(chat))
                .build();
    }

    @DeleteMapping("/delete/{chatId}")
    ApiResponse<Void> deleteChat(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId, reqUser.getId());

        return  ApiResponse.<Void>builder()
                .code(1000)
                .message("delete chat with id " + chatId + " successfully!")
                .build();
    }

}

