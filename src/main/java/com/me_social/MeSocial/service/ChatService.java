package com.me_social.MeSocial.service;

import com.me_social.MeSocial.entity.dto.request.GroupChatRequest;
import com.me_social.MeSocial.entity.modal.Chat;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;

    public Chat createChat(User reqUser, Long userId2) {
        User user = userService.findById(userId2);
        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);

        if (isChatExist != null) {
            return isChatExist;
        }

        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);

        return chat;
    }

    public Chat findChatById(Long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            return chat.get();
        }
        throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
    }

    public List<Chat> findAllChatByUserId(Long userId) {
        User user = userService.findById(userId);

        return chatRepository.findChatByUserId(user.getId());
    }

    public Chat createGroup(GroupChatRequest request, User reqUser) {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChatImage(request.getChatImage());
        group.setChatName(request.getChatName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        for (Long userId : request.getUserIds()) {
            User user = userService.findById(userId);
            group.getUsers().add(user);
        }

        return group;
    }

    public Chat addUserToGroup(Long userId, Long chatId, User reqUser) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        User user = userService.findById(userId);

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            } else {
                throw new AppException(ErrorCode.NOT_ADMIN);
            }
        }

        throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
    }

    public Chat renameGroup(Long chatId, String groupName, User reqUser) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getUsers().contains(reqUser)) {
                chat.setChatName(groupName);
                return chatRepository.save(chat);
            } else {
                throw new AppException(ErrorCode.NOT_MEMBER);
            }
        }
        throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
    }

    public Chat removeFromGroup(Long chatId, Long userId, User reqUser) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        User user = userService.findById(userId);

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            } else if (chat.getUsers().contains(reqUser)) {
                if (user.getId().equals(reqUser.getId())) {
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
            } else {
                throw new AppException(ErrorCode.NOT_ADMIN);
            }
        }

        throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
    }

    public void deleteChat(Long chatId, Long userId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        optionalChat.ifPresent(chatRepository::delete);
    }
}
