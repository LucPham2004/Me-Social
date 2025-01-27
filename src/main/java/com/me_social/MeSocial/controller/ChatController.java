package com.me_social.MeSocial.controller;

import com.me_social.MeSocial.entity.dto.request.GroupChatRequest;
import com.me_social.MeSocial.entity.dto.request.SingleChatRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Chat;
import com.me_social.MeSocial.entity.modal.User;
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

    @PostMapping("/single")
    ApiResponse<Chat> createChat(@RequestBody SingleChatRequest singleChatRequest, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);

        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());

        return  ApiResponse.<Chat>builder()
                .code(1000)
                .message("create chat successfully!")
                .result(chat)
                .build();
    }

    @PostMapping("/group")
    ApiResponse<Chat> createGroup(@RequestBody GroupChatRequest groupChatRequest, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);

        Chat chat = chatService.createGroup(groupChatRequest, reqUser);

        return  ApiResponse.<Chat>builder()
                .code(1000)
                .message("create group chat successfully!")
                .result(chat)
                .build();
    }

    @GetMapping("/{chatId}")
    ApiResponse<Chat> findChatById(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        Chat chat = chatService.findChatById(chatId);

        return  ApiResponse.<Chat>builder()
                .code(1000)
                .message("get chat with id " + chatId + " successfully!")
                .result(chat)
                .build();
    }

    @GetMapping("/user")
    ApiResponse<List<Chat>> findAllChatsByUser(@AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);
        List<Chat> chatList = chatService.findAllChatByUserId(reqUser.getId());

        return  ApiResponse.<List<Chat>>builder()
                .code(1000)
                .message("get all chats for user with email " + reqUser.getEmail() + " successfully!")
                .result(chatList)
                .build();
    }

    @PutMapping("/{chatId}/add/{userId}")
    ApiResponse<Chat> addUserToGroupChat(@PathVariable Long chatId, @PathVariable Long userId, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(userId, chatId, reqUser);

        return  ApiResponse.<Chat>builder()
                .code(1000)
                .message("add user to group chat successfully!")
                .result(chat)
                .build();
    }

    @PutMapping("/{chatId}/remove/{userId}")
    ApiResponse<Chat> removeUserFromGroupChat(@PathVariable Long chatId, @PathVariable Long userId, @AuthenticationPrincipal Jwt jwt) {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(userId, chatId, reqUser);

        return  ApiResponse.<Chat>builder()
                .code(1000)
                .message("remove user from group successfully!")
                .result(chat)
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

